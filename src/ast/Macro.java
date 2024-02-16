package ast;

import ast.condition.AbstractCondition;
import libs.Node;
import libs.ProgramScope;

import java.util.List;
import java.util.Objects;

/**
 * Representation of a function with a set of parameters, returning a boolean value
 * when evaluated in scope
 */
public class Macro extends Node {
    private final String name;
    private final List<String> parameters;
    private final AbstractCondition childCondition;

    public Macro(String name, List<String> parameters, AbstractCondition childCondition) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Macro macro = (Macro) o;
        return Objects.equals(parameters, macro.parameters) && Objects.equals(childCondition, macro.childCondition);
    }

    @Override
    public String toString() {
        return "Macro{" +
                "parameters=" + parameters +
                ", childCondition=" + childCondition +
                '}';
    }
}
