package parser;

import ast.Program;
import libs.Node;

public class ParseTreeToAST extends DSLParserBaseVisitor<Node> {

    @Override
    public Program visitProgram(DSLParser.ProgramContext ctx) {
        // TODO: Implement parse tree visitor
        return null;
    }

}
