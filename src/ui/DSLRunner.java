package ui;

import ast.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import parser.DSLLexer;
import parser.DSLParser;
import parser.ParseTreeToAST;

public class DSLRunner {

    public String run(String input) {
        System.out.println("Hello World!");
        DSLLexer lexer = new DSLLexer(CharStreams.fromString(input));
        TokenStream tokens = new CommonTokenStream(lexer);
        System.out.println("Done tokenizing");
        DSLParser parser = new DSLParser(tokens);
        ParseTreeToAST visitor = new ParseTreeToAST();
        Program parsedProgram = visitor.visitProgram(parser.program());
        System.out.println("Done Parsing");
        // TODO: Add evaluate

        return "Evaluation not implemented";
    }
}
