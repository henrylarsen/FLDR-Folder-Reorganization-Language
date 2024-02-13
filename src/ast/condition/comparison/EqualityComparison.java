package ast.condition.comparison;

import ast.operand.Operand;
import libs.value.Value;

/**
 * For comparing between two operands for strict equality where
 * both type and value
 */
public class EqualityComparison extends AbstractComparison {
    public EqualityComparison(Operand leftOperand, Operand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean compare(Value leftValue, Value rightValue) {
        return leftValue.getValue().equals(rightValue.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public String toString() {
        return "EqualityComparison{" +
                "leftOperand=" + leftOperand +
                ", rightOperand=" + rightOperand +
                '}';
    }
}
