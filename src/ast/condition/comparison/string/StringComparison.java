package ast.condition.comparison.string;

import ast.condition.comparison.AbstractComparison;
import ast.operand.Operand;
import libs.value.Value;

/**
 * For comparing two string(able) operands
 *
 * @see StringComparisonType
 */
public class StringComparison extends AbstractComparison {
    private final StringComparisonType comparisonType;

    public StringComparison(Operand leftOperand, Operand rightOperand, StringComparisonType comparisonType) {
        super(leftOperand, rightOperand);
        this.comparisonType = comparisonType;
    }

    @Override
    protected boolean compare(Value leftValue, Value rightValue) {
        return comparisonType.compare(leftValue.coerceToString(), rightValue.coerceToString());
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        StringComparison that = (StringComparison) o;
        return comparisonType == that.comparisonType;
    }

    @Override
    public String toString() {
        return "StringComparison{" +
                "comparisonType=" + comparisonType +
                ", leftOperand=" + leftOperand +
                ", rightOperand=" + rightOperand +
                '}';
    }
}
