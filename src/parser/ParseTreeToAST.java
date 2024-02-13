package parser;

import ast.Macro;
import ast.Program;
import ast.condition.AbstractCondition;
import ast.condition.MacroCallCondition;
import ast.condition.NegationCondition;
import ast.condition.comparison.EqualityComparison;
import ast.condition.comparison.numeric.NumericComparison;
import ast.condition.comparison.numeric.NumericComparisonType;
import ast.condition.comparison.string.StringComparison;
import ast.condition.comparison.string.StringComparisonType;
import ast.condition.junction.ConditionJunction;
import ast.condition.junction.ConditionJunctionType;
import ast.folder.AbstractFolder;
import ast.operand.ConstantOperand;
import ast.operand.Operand;
import ast.operand.TemplateOperand;
import ast.operand.VariableOperand;
import libs.Node;
import libs.value.IntegerValue;
import libs.value.StringValue;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeToAST extends DSLParserBaseVisitor<Node> {

    @Override
    public Program visitProgram(DSLParser.ProgramContext ctx) {
        List<Macro> macros = new ArrayList<>();
        for (DSLParser.ConditionContext macro : ctx.condition()) {
            macros.add((Macro) macro.accept(this));
        }
        List<AbstractFolder> folders = new ArrayList<>();
        for (DSLParser.FoldersContext folder : ctx.folders()) {
            folders.add((AbstractFolder) folder.accept(this));
        }
        return new Program(macros, folders);
    }

    @Override
    public Macro visitCondition(DSLParser.ConditionContext ctx) {
        List<TerminalNode> symbols = ctx.condition_decl().condition_params().TEXT();
        List<String> params = symbols.stream().map(Object::toString).toList();
        AbstractCondition condition = (AbstractCondition) ctx.condition_body().accept(this);
        return new Macro(params, condition);
    }

    @Override
    public AbstractCondition visitCondition_body(DSLParser.Condition_bodyContext ctx) {
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
        AbstractCondition rsf;
        if (ctx.one_of() != null) {
            rsf = (AbstractCondition) ctx.one_of().accept(this);
        } else {
            rsf = (AbstractCondition) ctx.singular_check().accept(this);
        }

        if (ctx.NOT() != null) {
            return new NegationCondition(rsf);
        } else {
            return rsf;
        }
    }

    @Override
    public AbstractCondition visitOne_of(DSLParser.One_ofContext ctx) {
        // TODO
        return null;
    }

    @Override
    public AbstractCondition visitSingular_check(DSLParser.Singular_checkContext ctx) {
        if (ctx.comparison() != null) { // input comparison
            Operand l = (Operand) ctx.input().accept(this);
            Operand r = (Operand) ctx.comparison().input().accept(this);
            DSLParser.OperatorContext operator = ctx.comparison().operator();
            if (operator.COMP_E() != null) {
                return new NumericComparison(l, r, NumericComparisonType.EQUAL_TO);
            } else if (operator.COMP_G() != null) {
                return new NumericComparison(l, r, NumericComparisonType.GREATER_THAN);
            } else if (operator.COMP_L() != null) {
                return new NumericComparison(l, r, NumericComparisonType.LESS_THAN);
            } else if (operator.INCLUDES() != null) {
                return new StringComparison(l, r, StringComparisonType.CONTAINS);
            } else if (operator.IS() != null) {
                return new EqualityComparison(l, r);
            }
            // Should be unreachable
            throw new IllegalArgumentException("Illegal operator at parsing. Implement in visitSingular_check");
        } else { // TEXT function
            String funName = ctx.TEXT().toString();
            List<Operand> rands = ctx.function().function_params().input().stream().map(f -> (Operand) f.accept(this)).toList();
            return new MacroCallCondition(funName, rands);
        }
    }

    @Override
    public Operand visitInput(DSLParser.InputContext ctx) {
        if (ctx.string() != null) { // String (possibly template string)
            DSLParser.String_bodyContext strctx = ctx.string().string_body();
            StringBuilder resultStr = new StringBuilder();
            List<String> vars = new ArrayList<>();
            boolean isTemplate = false;
            for (ParseTree tree : strctx.children) {
                if (tree instanceof TerminalNode t) {
                    resultStr.append(t);
                } else if (tree instanceof DSLParser.String_varContext var) {
                    vars.add(var.STRING_TEXT().toString());
                    resultStr.append("$");
                    isTemplate = true;
                }
            }

            if (isTemplate) {
                return new TemplateOperand(vars.stream().map(VariableOperand::new).toList(), resultStr.toString());
            } else {
                return new ConstantOperand(new StringValue(resultStr.toString()));
            }
        } else if (ctx.INT() != null) { // Integer
            return new ConstantOperand(new IntegerValue(Integer.parseInt(ctx.INT().toString().trim())));
        } else { // Variable
            return new VariableOperand(ctx.var().VAR_TEXT().toString());
        }
    }

    @Override
    public AbstractFolder visitFolder(DSLParser.FolderContext ctx) {
        // TODO
        return null;
    }

}
