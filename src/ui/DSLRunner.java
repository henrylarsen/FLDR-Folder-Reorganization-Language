package ui;

import ast.Program;
import libs.ProgramScope;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import parser.DSLLexer;
import parser.DSLParser;
import parser.ParseTreeToAST;

import java.nio.file.Path;
import java.util.Map;

public class DSLRunner {

    public Map<Path,Path> preview(String input) {
        System.out.println("Hello World!");
        DSLLexer lexer = new DSLLexer(CharStreams.fromString(input));
        TokenStream tokens = new CommonTokenStream(lexer);
        System.out.println("Done tokenizing");
        DSLParser parser = new DSLParser(tokens);
        ParseTreeToAST visitor = new ParseTreeToAST();
        Program program = visitor.visitProgram(parser.program());
        System.out.println("Done Parsing: " + program);
        Map<Path, Path> result = program.evaluate(new ProgramScope());
        System.out.println("Done evaluating: " + result);
        return result;
    }

    public String run(String input) {
        Map<Path, Path> result = preview(input);
        // TODO: Actually move files
        return "Not implemented";
    }
}
