package ast.condition;

import libs.Node;
import libs.ProgramScope;

/**
 * Represents any construct that can be evaluated to a boolean
 * given a variable scope
 */
public abstract class AbstractCondition extends Node {
    /**
     * Evaluates the condition given a provided variable scope
     * @return the result of the condition
     */
    public abstract boolean evaluate(ProgramScope scope);
}
