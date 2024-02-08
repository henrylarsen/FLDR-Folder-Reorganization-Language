package parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import java.util.Stack;

public class ParserDirectTest {

    DSLParser.ProgramContext parseExample() {
        String slideInput = """
                RESTRUCTURE path
                
                CONDITION new_condition(param_1, param2) : 0 > {param_1} AND {TYPE} IS "png"
                
                FOLDER "folder1"
                    CONTAINS: {DATE_YEAR} = 2020 OR new_condition(0, "string")
                    HAS SUBFOLDERS
                        FOLDER "folder_1"
                            CONTAINS: {NAME} INCLUDES "cat"
                        FOREACH file_type in ["pdf", "png", "jpg"]
                            FOLDER "folder_{file_type}5"
                                CONTAINS: new_condition(2, {file_type})
                                
                FOLDER "folder2"
                """;
        DSLLexer lexer = new DSLLexer(CharStreams.fromString(slideInput));
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        DSLParser parser = new DSLParser(tokens);
        return parser.program();
    }

    /**
     * Tests to visualize the parse tree
     */

    @Test
    void lectureExampleTest() {
        DSLParser.ProgramContext p = parseExample();

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
