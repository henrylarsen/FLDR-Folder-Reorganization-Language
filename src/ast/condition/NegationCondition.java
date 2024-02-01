package ast.condition;

import libs.ProgramScope;

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
}
