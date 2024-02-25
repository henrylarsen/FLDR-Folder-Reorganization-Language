package parser;

import ast.Macro;
import ast.Program;
import ast.condition.*;
import ast.condition.comparison.EqualityComparison;
import ast.condition.comparison.numeric.NumericComparison;
import ast.condition.comparison.numeric.NumericComparisonType;
import ast.condition.comparison.string.StringComparison;
import ast.condition.comparison.string.StringComparisonType;
import ast.condition.junction.ConditionJunction;
import ast.condition.junction.ConditionJunctionType;
import ast.folder.AbstractFolder;
import ast.folder.ForEachFolder;
import ast.folder.SingleFolder;
import ast.operand.ConstantOperand;
import ast.operand.Operand;
import ast.operand.TemplateOperand;
import ast.operand.VariableOperand;
import libs.Node;
import libs.SizeConverter;
import libs.value.LongValue;
import libs.value.StringValue;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeToAST extends DSLParserBaseVisitor<Node> {

    @Override
    public Program visitProgram(DSLParser.ProgramContext ctx) {
        String path = ctx.start_path().PATH().toString().trim();
        List<Macro> macros = new ArrayList<>();
        for (DSLParser.ConditionContext macro : ctx.condition()) {
            macros.add((Macro) macro.accept(this));
        }
        List<AbstractFolder> folders = new ArrayList<>();
        for (DSLParser.FoldersContext folder : ctx.folders()) {
            folders.add((AbstractFolder) folder.accept(this));
        }
        return new Program(path, macros, folders);
    }

    // ------------------------------- Conditions -------------------------------------
    @Override
    public Macro visitCondition(DSLParser.ConditionContext ctx) {
        String name = ctx.condition_decl().TEXT().toString().trim();
        List<String> params = List.of();
        if (ctx.condition_decl().condition_params() != null) {
            List<TerminalNode> symbols = ctx.condition_decl().condition_params().TEXT();
            params = symbols.stream().map(Object::toString).map(String::trim).toList();
        }
        AbstractCondition condition = (AbstractCondition) ctx.condition_body().accept(this);
        return new Macro(name, params, condition);
    }

    @Override
    public AbstractCondition visitCondition_body(DSLParser.Condition_bodyContext ctx) {
        if (ctx.OTHER() != null) {
            return new CatchAllCondition();
        }
        AbstractCondition result = (AbstractCondition) ctx.boolean_().accept(this);
        for (int i = 0; i < ctx.junction().size(); i++) {
            AbstractCondition cond = (AbstractCondition) ctx.condition_body(i).accept(this);
            if (ctx.junction(i).OR() != null) {
                result = new ConditionJunction(ConditionJunctionType.OR, result, cond);
            } else {
                result = new ConditionJunction(ConditionJunctionType.AND, result, cond);
            }
        }
        return result;
    }

    @Override
    public AbstractCondition visitBoolean(DSLParser.BooleanContext ctx) {
        AbstractCondition rsf = (AbstractCondition) ctx.singular_check().accept(this);
        if (ctx.NOT() != null) {
            return new NegationCondition(rsf);
        } else {
            return rsf;
        }
    }

    @Override
    public AbstractCondition visitSingular_check(DSLParser.Singular_checkContext ctx) {
        if (ctx.input() != null) { // input ...
            Operand l = (Operand) ctx.input().accept(this);
            if (ctx.comparison() != null) { // input comparison
                Operand r = (Operand) ctx.comparison().input().accept(this);
                DSLParser.OperatorContext operator = ctx.comparison().operator();
                if (operator.COMP_E() != null) {
                    return new NumericComparison(l, r, NumericComparisonType.EQUAL_TO);
                } else if (operator.COMP_G() != null) {
                    return new NumericComparison(l, r, NumericComparisonType.GREATER_THAN);
                } else if (operator.COMP_L() != null) {
                    return new NumericComparison(l, r, NumericComparisonType.LESS_THAN);
                } else if (operator.COMP_GE() != null) {
                    return new NumericComparison(l, r, NumericComparisonType.GREATER_THAN_EQUAL);
                } else if (operator.COMP_LE() != null) {
                    return new NumericComparison(l, r, NumericComparisonType.LESS_THAN_EQUAL);
                } else if (operator.INCLUDES() != null) {
                    return new StringComparison(l, r, StringComparisonType.CONTAINS);
                } else if (operator.IS() != null) {
                    return new EqualityComparison(l, r);
                }
                // Should be unreachable
                throw new IllegalArgumentException("Illegal operator at parsing. Implement in visitSingular_check");
            } else { // input one_of
                List<Operand> rights = ctx.one_of().list().list_contents().input()
                        .stream().map(in -> (Operand) in.accept(this)).toList();
                return new OneOfCondition(l, rights);
            }
        } else { // TEXT function
            String funName = ctx.TEXT().toString().trim();
            List<Operand> rands = List.of();
            if (ctx.function().function_params() != null) {
                rands = ctx.function().function_params().input()
                        .stream().map(f -> (Operand) f.accept(this)).toList();
            }
            return new MacroCallCondition(funName, rands);
        }
    }

    // ----------------------------------- Operands ---------------------------------------

    @Override
    public Operand visitInput(DSLParser.InputContext ctx) {
        if (ctx.string() != null) { // String (possibly template string)
            return (Operand) ctx.string().accept(this);
        } else if (ctx.size() != null) { // Size

            if (ctx.size().SIZE_GB() != null) { // GB
                String val = ctx.size().SIZE_GB().toString().trim();
                return new ConstantOperand(new LongValue(SizeConverter.convertToBytes(val)));
            } else if (ctx.size().SIZE_MB() != null) { // MB
                String val = ctx.size().SIZE_MB().toString().trim();
                return new ConstantOperand(new LongValue(SizeConverter.convertToBytes(val)));
            } else if (ctx.size().SIZE_KB() != null) { // KB
                String val = ctx.size().SIZE_KB().toString().trim();
                return new ConstantOperand(new LongValue(SizeConverter.convertToBytes(val)));
            } else { // B
                String val = ctx.size().SIZE_B().toString().trim();
                return new ConstantOperand(new LongValue(SizeConverter.convertToBytes(val)));
            }
        } else if (ctx.INT() != null) { // Long
            return new ConstantOperand(new LongValue(Long.parseLong(ctx.INT().toString().trim())));
        } else { // Variable
            return new VariableOperand(ctx.var().VAR_TEXT().toString().trim());
        }
    }

    @Override
    public Operand visitString(DSLParser.StringContext ctx) {
        StringBuilder resultStr = new StringBuilder();
        List<String> vars = new ArrayList<>();
        boolean isTemplate = false;
        for (ParseTree tree : ctx.string_body().children) {
            // While not ideal, this is seemingly the best way to iterate through all tree children in tandem
            if (tree instanceof TerminalNode t) {
                resultStr.append(t);
            } else if (tree instanceof DSLParser.String_varContext var) {
                vars.add(var.STRING_TEXT().toString().trim());
                resultStr.append("$");
                isTemplate = true;
            }
        }

        if (isTemplate) {
            return new TemplateOperand(vars.stream().map(VariableOperand::new).toList(), resultStr.toString().trim());
        } else {
            return new ConstantOperand(new StringValue(resultStr.toString().trim()));
        }
    }

    // ---------------------------------- Folders ---------------------------------------
    @Override
    public AbstractFolder visitFolders(DSLParser.FoldersContext ctx) {
        if(ctx.folder() != null) { // single folder
            return (AbstractFolder) ctx.folder().accept(this);
        } else { // for each
            String name = ctx.for_loop().TEXT().toString().trim();
            List<Operand> operands = ctx.for_loop().list().list_contents().input()
                    .stream().map(d -> (Operand) d.accept(this)).toList();
            List<AbstractFolder> subs = new ArrayList<>();
            if (ctx.for_loop().folder() != null) {
                subs.add((AbstractFolder) ctx.for_loop().folder().accept(this));
            }
            return new ForEachFolder(name, operands, subs);
        }
    }

    @Override
    public AbstractFolder visitFolder(DSLParser.FolderContext ctx) {
        Operand name;
        if (ctx.string() != null) { // string
            name = (Operand) ctx.string().accept(this);
        } else { // var
            name = new VariableOperand(ctx.var().VAR_TEXT().toString().trim());
        }
        AbstractCondition cond = null;
        if (ctx.contains() != null) {
            cond = (AbstractCondition) ctx.contains().condition_body().accept(this);
        }

        List<AbstractFolder> subs = new ArrayList<>();
        if (ctx.subfolders() != null) {
            for (DSLParser.FoldersContext folders : ctx.subfolders().folders()) {
                subs.add((AbstractFolder) folders.accept(this));
            }

        }
        return new SingleFolder(name, cond, subs);
    }

}
