package parser;

import ast.Program;
import ast.condition.AbstractCondition;
import ast.condition.comparison.numeric.NumericComparison;
import ast.condition.comparison.numeric.NumericComparisonType;
import ast.condition.comparison.string.StringComparison;
import ast.condition.comparison.string.StringComparisonType;
import ast.condition.junction.ConditionJunction;
import ast.condition.junction.ConditionJunctionType;
import ast.folder.ForEachFolder;
import ast.folder.SingleFolder;
import ast.operand.ConstantOperand;
import ast.operand.TemplateOperand;
import ast.operand.VariableOperand;
import libs.Node;
import libs.value.IntegerValue;
import libs.value.StringValue;
import libs.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ParserTest {
    Node newpdfsProgram;
    Node forEachProgram;
    AbstractCondition isPdf;
    AbstractCondition isNew;
    AbstractCondition isNewAndPdf;

    @BeforeEach
    public void initialize() {
        isPdf = new StringComparison(
                new VariableOperand("TYPE"),
                new ConstantOperand(new StringValue("pdf")),
                StringComparisonType.IS_IGNORE_CASE
        );

        isNew = new NumericComparison(
                new VariableOperand("DATE"),
                new ConstantOperand(new IntegerValue(20240101)),
                NumericComparisonType.GREATER_THAN
        );

        isNewAndPdf = new ConditionJunction(ConditionJunctionType.AND, isPdf, isNew);

        newpdfsProgram = new Program(
                Collections.emptyList(),
                Collections.singletonList(new SingleFolder(
                        new ConstantOperand(new StringValue("pdfs folder")),
                        isNewAndPdf,
                        Collections.emptyList()
                ))
        );

        Value[] categories = {new StringValue("school"), new StringValue("work"), new StringValue("home")};
        forEachProgram = new Program(
                new ArrayList<>(),
                Collections.singletonList(new ForEachFolder(
                        "cat",
                        Arrays.asList(categories),
                        Collections.singletonList(new SingleFolder(
                                new TemplateOperand(new VariableOperand("cat"), "$-folder"),
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
    void helloWorld() {
        System.out.println("Hello world!");
    }
}