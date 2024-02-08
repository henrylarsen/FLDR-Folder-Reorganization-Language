package ast.condition;

import libs.ProgramScope;

import java.util.Objects;

/**
 * Represents a condition that is negated, i.e.:
 * NOT (...)
 */
public class NegationCondition extends AbstractCondition {
    private AbstractCondition negatedCondition;

    public NegationCondition(AbstractCondition negatedCondition) {
        this.negatedCondition = negatedCondition;
    }

    @Override
    public boolean evaluate(ProgramScope scope) {
        return !negatedCondition.evaluate(scope);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NegationCondition that = (NegationCondition) o;
        return Objects.equals(negatedCondition, that.negatedCondition);
    }
}
