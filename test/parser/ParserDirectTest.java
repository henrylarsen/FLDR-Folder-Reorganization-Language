package parser;

import ast.Macro;
import ast.Program;
import ast.condition.comparison.EqualityComparison;
import ast.condition.comparison.numeric.NumericComparison;
import ast.condition.comparison.numeric.NumericComparisonType;
import ast.condition.junction.ConditionJunction;
import ast.condition.junction.ConditionJunctionType;
import ast.operand.ConstantOperand;
import ast.operand.VariableOperand;
import libs.value.IntegerValue;
import libs.value.StringValue;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class ParserDirectTest {

    DSLParser.ProgramContext parseExample() {
        String slideInput = """
                RESTRUCTURE "C:\\Users\\Henry\\OneDrive - UBC\\Desktop\\Cover Letters\\ICBC Full Stack - Cover Letter.docx"
                
                CONDITION new_condition(param_1, param2) : 0 > {param_1} AND {TYPE} IS "png" OR {DATE_YEAR} > 2022
                
                FOLDER "folder1 fold"
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

    Program result() {
        return new Program(List.of(new Macro(
                List.of("param_1", "param2"),
                new ConditionJunction(
                        ConditionJunctionType.AND,
                        new NumericComparison(
                                new ConstantOperand(new IntegerValue(0)),
                                new VariableOperand("param_1"),
                                NumericComparisonType.GREATER_THAN),
                        new ConditionJunction(
                                ConditionJunctionType.OR,
                                new EqualityComparison(
                                        new VariableOperand("TYPE"),
                                        new ConstantOperand(new StringValue("png"))
                                ),
                                new NumericComparison(
                                        new VariableOperand("DATE_YEAR"),
                                        new ConstantOperand(new IntegerValue(2022)),
                                        NumericComparisonType.GREATER_THAN
                                )
                        )
                ))),
                Collections.emptyList()
        );
    }

    @Test
    void parseTreeTest() {
        DSLParser.ProgramContext p = parseExample();

        ParseTreeToAST visitor = new ParseTreeToAST();
        //assertEquals(visitor.visitProgram(p), result());
        System.out.println(visitor.visitProgram(p));
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
