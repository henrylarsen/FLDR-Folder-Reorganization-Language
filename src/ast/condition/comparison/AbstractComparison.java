package ast.condition.comparison;

import ast.condition.AbstractCondition;
import ast.operand.Operand;
import libs.ProgramScope;
import libs.value.Value;

import java.util.Objects;

/**
 * An abstraction of a comparison between two operands
 */
public abstract class AbstractComparison extends AbstractCondition {
    protected Operand leftOperand;
    protected Operand rightOperand;

    public AbstractComparison(Operand leftOperand, Operand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public boolean evaluate(ProgramScope scope) {
        Value leftValue = leftOperand.getValue(scope);
        Value rightValue = rightOperand.getValue(scope);

        return this.compare(leftValue, rightValue);
    }

    protected abstract boolean compare(Value leftValue, Value rightValue);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractComparison that = (AbstractComparison) o;
        return Objects.equals(leftOperand, that.leftOperand) && Objects.equals(rightOperand, that.rightOperand);
    }
}
