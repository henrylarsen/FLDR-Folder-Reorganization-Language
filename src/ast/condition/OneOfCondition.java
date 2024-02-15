package ast.condition;

import ast.operand.Operand;
import libs.ProgramScope;

import java.util.List;
import java.util.Objects;

/**
 * leftOperand IS ONE OF rightOperand
 *
 * where rightOperand is some list of operands: [..., ..., ...]
 */
public class OneOfCondition extends AbstractCondition {
    private final Operand leftOperand;
    private final List<Operand> rightOperand;

    public OneOfCondition(Operand leftOperand, List<Operand> rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public boolean evaluate(ProgramScope scope) {
        Object leftValue = leftOperand.getValue(scope).getValue();

        for (Operand options : rightOperand) {
            if (leftValue.equals(options.getValue(scope).getValue())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OneOfCondition that = (OneOfCondition) o;
        return Objects.equals(leftOperand, that.leftOperand) && Objects.equals(rightOperand, that.rightOperand);
    }

    @Override
    public String toString() {
        return "OneOfCondition{" +
                "leftOperand=" + leftOperand +
                ", rightOperand=" + rightOperand +
                '}';
    }
}
