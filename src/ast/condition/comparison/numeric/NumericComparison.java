package ast.condition.comparison.numeric;

import ast.condition.comparison.AbstractComparison;
import ast.operand.Operand;
import libs.value.Value;

/**
 * For comparing between two int(able) operands
 *
 * @see NumericComparisonType
 */
public class NumericComparison extends AbstractComparison {
    private final NumericComparisonType comparisonType;

    public NumericComparison(Operand leftOperand, Operand rightOperand, NumericComparisonType comparisonType) {
        super(leftOperand, rightOperand);
        this.comparisonType = comparisonType;
    }

    @Override
    protected boolean compare(Value leftValue, Value rightValue) {
        return comparisonType.compare(leftValue.coerceToInt(), rightValue.coerceToInt());
    }
}