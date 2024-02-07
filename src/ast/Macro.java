package ast;

import ast.condition.AbstractCondition;
import libs.ProgramScope;

import java.util.List;

/**
 * Representation of a function with a set of parameters, returning a boolean value
 * when evaluated in scope
 */
public class Macro extends Definition {
    private final List<String> parameters;
    private final AbstractCondition childCondition;

    public Macro(List<String> parameters, AbstractCondition childCondition) {
        this.parameters = parameters;
        this.childCondition = childCondition;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public boolean evaluate(ProgramScope scope) {
        for (String parameter : parameters) {
            if (scope.hasDefinition(parameter)) {
                continue;
            }

            throw new IllegalStateException("Macro defined without necessary parameter " + parameter + " in scope!");
        }

        return childCondition.evaluate(scope);
    }
}
