package parser;

import ast.Program;
import ast.condition.AbstractCondition;
import ast.condition.comparison.EqualityComparison;
import ast.condition.comparison.numeric.NumericComparison;
import ast.condition.comparison.numeric.NumericComparisonType;
import ast.condition.comparison.string.StringComparison;
import ast.condition.comparison.string.StringComparisonType;
import ast.condition.junction.ConditionJunction;
import ast.condition.junction.ConditionJunctionType;
import ast.folder.ForEachFolder;
import ast.folder.SingleFolder;
import ast.operand.ConstantOperand;
import ast.operand.Operand;
import ast.operand.TemplateOperand;
import ast.operand.VariableOperand;
import libs.Node;
import libs.value.LongValue;
import libs.value.StringValue;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    Node newpdfsProgram;
    Node forEachProgram;
    AbstractCondition isPdf;
    AbstractCondition isNew;
    AbstractCondition isNewAndPdf;

    private Program parse(String input) {
        DSLLexer lexer = new DSLLexer(CharStreams.fromString(input));
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        DSLParser parser = new DSLParser(tokens);
        ParseTreeToAST visitor = new ParseTreeToAST();
        Program program = (Program) parser.program().accept(visitor);

        System.out.println("Parsed: " + program);
        return program;
    }

    @BeforeEach
    public void initialize() {
        isPdf = new EqualityComparison(
                new VariableOperand("TYPE"),
                new ConstantOperand(new StringValue("pdf"))
        );

        isNew = new NumericComparison(
                new VariableOperand("DATE"),
                new ConstantOperand(new LongValue(20240101)),
                NumericComparisonType.GREATER_THAN
        );

        isNewAndPdf = new ConditionJunction(ConditionJunctionType.AND, isPdf, isNew);

        newpdfsProgram = new Program("test",
                Collections.emptyList(),
                Collections.singletonList(new SingleFolder(
                        new ConstantOperand(new StringValue("pdfs folder")),
                        isNewAndPdf,
                        Collections.emptyList()
                ))
        );

        Operand[] categories = {
                new ConstantOperand(new StringValue("school")),
                new ConstantOperand(new StringValue("work")),
                new ConstantOperand(new StringValue("home"))};
        forEachProgram = new Program(
                "test",
                new ArrayList<>(),
                Collections.singletonList(new ForEachFolder(
                        "cat",
                        Arrays.asList(categories),
                        Collections.singletonList(new SingleFolder(
                                new TemplateOperand(List.of(new VariableOperand("cat")), "$-folder"),
                                new StringComparison(
                                        new VariableOperand("NAME"),
                                        new VariableOperand("cat"),
                                        StringComparisonType.CONTAINS
                                ),
                                Collections.emptyList()
                        ))
                ))
        );
    }

    @Test
    public void testNewPdfsProgram() {
        String input = """
                RESTRUCTURE C:\\Users\\Henry\\OneDrive - UBC\\Desktop\\Cover Letters\\ICBC Full Stack - Cover Letter.docx
                                
                FOLDER "pdfs folder"
                    CONTAINS: {TYPE} IS "pdf" AND {DATE} > 20240101
                """;

        assertEquals(parse(input), newpdfsProgram);
    }
}