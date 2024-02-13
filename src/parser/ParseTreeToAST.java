package parser;

import ast.Macro;
import ast.Program;
import ast.condition.AbstractCondition;
import ast.condition.MacroCallCondition;
import ast.condition.NegationCondition;
import ast.condition.junction.ConditionJunction;
import ast.condition.junction.ConditionJunctionType;
import ast.folder.AbstractFolder;
import ast.operand.Operand;
import libs.Node;
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
        Operand l = (Operand) ctx.input().accept(this);
        if (ctx.comparison() != null) {
            return null;
        } else {
            ctx.function().function_params();
            // TODO: This line is incredibly janky but it's currently necessary because of how we parse the string
            return new MacroCallCondition(l.getValue(null).coerceToString(), null);
        }
    }

    @Override
    public Operand visitInput(DSLParser.InputContext ctx) {
        // TODO
        return null;
    }

}
