package ast.condition;

import ast.operand.VariableOperand;
import libs.ProgramScope;

import java.util.Objects;

/**
 * A condition that evaluates to true iff a variable exists in the scope
 *
 * EXISTS $variable
 */
public class ExistsCondition extends AbstractCondition {
    private final VariableOperand variable;

    public ExistsCondition(VariableOperand variable) {
        this.variable = variable;
    }

    @Override
    public boolean evaluate(ProgramScope scope) {
        return variable.exists(scope);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExistsCondition that = (ExistsCondition) o;
        return Objects.equals(variable, that.variable);
    }
}
