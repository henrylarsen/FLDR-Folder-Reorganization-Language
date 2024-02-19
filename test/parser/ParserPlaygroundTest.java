package parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import java.util.Stack;

// Tests in this file don't have assertions, they're just for trying things and printing results
public class ParserPlaygroundTest {

    DSLParser.ProgramContext parseExample() {
        String slideInput = """
        RESTRUCTURE ./DOWNLOADS
        
        CONDITION is_during_year(year): {FILE_YEAR} = {year}
        
        FOLDER "DOWNLOADS-SORTED"
            CONTAINS: {FILE_NAME} INCLUDES "cat"
            HAS SUBFOLDERS [
                FOREACH course_name IN ["info300", "phil230"]
                           FOLDER {course_name}
                                CONTAINS: {FILE_NAME} INCLUDES {course_name}
                FOLDER "gpxes"
                    CONTAINS: {FILE_TYPE} IS "gpx" AND {FILE_DATE} >= 20080101
                FOLDER "2024 photos"
                    CONTAINS: is_during_year(2024) AND ({FILE_TYPE} ONEOF ["jpeg", "png", "jpg"])
                FOLDER "other"
                    CONTAINS: +OTHER
            ]
                """;
        DSLLexer lexer = new DSLLexer(CharStreams.fromString(slideInput));
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        DSLParser parser = new DSLParser(tokens);
        return parser.program();
    }

    DSLParser.ProgramContext parseExample2() {
        String slideInput = """
                RESTRUCTURE "C:\\Users\\Henry\\Desktop\\Courses"
                
                CONDITION new_condition(param_1, param2) : 0 > {param_1} AND {SIZE} > 200GB
                CONDITION no_params(): {FILE_NAME} INCLUDES "cat"
                
                FOLDER "folder1 fold"
                    CONTAINS: no_params("s{var}") AND 3GB > {FILE_SIZE} OR {NAME} INCLUDES "cat"
                    HAS SUBFOLDERS [
                        FOLDER "folder_1"
                            CONTAINS: {NAME} INCLUDES "cat"
                        FOREACH file_type IN ["pdf", "png", "jpg"]
                            FOLDER "folder_{file_type}5"
                                CONTAINS: new_condition(2, {file_type}) AND no_params()
                            
                        ]
                                              
                FOLDER "folder2"
                    CONTAINS: +OTHER
                """;
        DSLLexer lexer = new DSLLexer(CharStreams.fromString(slideInput));
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        DSLParser parser = new DSLParser(tokens);
        return parser.program();
    }

    DSLParser.ProgramContext parseExample3() {
        String slideInput = """
                RESTRUCTURE ./DOWNLOADS
                
                CONDITION is_during_year(year): {FILE_YEAR} = {year}
                
                FOLDER "DOWNLOADS-SORTED"
                CONTAINS: is_during_year(2020)
                HAS SUBFOLDERS [
                    FOREACH file_type IN ["png", "pdf", "jpg"]
                            FOLDER "2024-{file_type}"
                                CONTAINS: is_during_year(2024) AND {FILE_TYPE} IS {file_type}
                    FOLDER "some-files"
                        CONTAINS: {FILE_SIZE} > 200KB AND {FILE_SIZE} < 300KB
                    FOLDER "other"
                        CONTAINS: +OTHER
                ]

                """;
        DSLLexer lexer = new DSLLexer(CharStreams.fromString(slideInput));
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        DSLParser parser = new DSLParser(tokens);
        return parser.program();
    }

    /**
     * Print string representation of an ast
     */
    @Test
    void parseTreeTest() {
        DSLParser.ProgramContext p = parseExample();
        ParseTreeToAST visitor = new ParseTreeToAST();
        System.out.println(visitor.visitProgram(p));
    }

    @Test
    void parseTreeTest2() {
        DSLParser.ProgramContext p = parseExample2();
        ParseTreeToAST visitor = new ParseTreeToAST();
        System.out.println(visitor.visitProgram(p));
    }

    @Test
    void parseTreeTest3() {
        DSLParser.ProgramContext p = parseExample3();
        ParseTreeToAST visitor = new ParseTreeToAST();
        System.out.println(visitor.visitProgram(p));
    }

    /**
     * Tests to visualize the parse tree
     */
    @Test
    void lectureExampleTest() {
        DSLParser.ProgramContext p = parseExample2();

        Stack<Pair<ParseTree, Integer>> stack = new Stack<Pair<ParseTree, Integer>>();
        for (int i = 0; i < p.children.size(); i++) {
            ParseTree child = p.children.get(p.children.size() - i - 1);
            Pair<ParseTree, Integer> tuple = new Pair<>(child, 1);
            stack.push(tuple);
        }

        while(!stack.isEmpty()) {
            Pair<ParseTree, Integer> tuple = stack.pop();
            ParseTree node = tuple.a;
            Integer depth = tuple.b;
            for (int i = 0; i < node.getChildCount(); i++) {
                ParseTree child = node.getChild(node.getChildCount() - i - 1);
                Pair<ParseTree, Integer> input = new Pair<>(child, depth + 1);
                stack.push(input);
            }

            String className = node.getClass().getName();
            String simplifiedClassName;
            if (className.startsWith("parser.DSLParser")) {
                simplifiedClassName = className.substring(17, className.length() - 7);
            } else if (className.startsWith("org.antlr.v4.runtime.tree.TerminalNodeImpl")) {
                simplifiedClassName = node.getText().trim();
            } else {
                simplifiedClassName = className;
            }
            System.out.println("\t".repeat(depth) + simplifiedClassName + ": " + node.getText().trim());
        }
    }
}
