package ast.condition.junction;

import ast.condition.AbstractCondition;
import libs.ProgramScope;

import java.util.Objects;

/**
 * Represents a boolean junction (AND, OR, etc.) between two child conditions, e.g.
 *
 * ($NAME contains "a") AND ($NAME contains "b")
 */
public class ConditionJunction extends AbstractCondition {
    private ConditionJunctionType junctionType;
    private AbstractCondition leftCondition;
    private AbstractCondition rightCondition;

    public ConditionJunction(ConditionJunctionType junctionType, AbstractCondition leftCondition, AbstractCondition rightCondition) {
        this.junctionType = junctionType;
        this.leftCondition = leftCondition;
        this.rightCondition = rightCondition;
    }

    @Override
    public boolean evaluate(ProgramScope scope) {
        return junctionType.join(
                () -> leftCondition.evaluate(scope),
                () -> rightCondition.evaluate(scope)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionJunction that = (ConditionJunction) o;
        return junctionType == that.junctionType && Objects.equals(leftCondition, that.leftCondition) && Objects.equals(rightCondition, that.rightCondition);
    }

    @Override
    public String toString() {
        return "ConditionJunction{" +
                "junctionType=" + junctionType +
                ", leftCondition=" + leftCondition +
                ", rightCondition=" + rightCondition +
                '}';
    }
}
