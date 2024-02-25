package ast.operand;

import libs.ProgramScope;
import libs.value.Value;

import java.util.Objects;

/**
 * An operand referring to a variable
 */
public class VariableOperand extends Operand {
    private final String variableName;

    public VariableOperand(String variableName) {
        this.variableName = variableName;
    }

    public boolean exists(ProgramScope scope) {
        return scope.hasDefinition(variableName);
    }

    @Override
    public Value getValue(ProgramScope scope) {
        return scope.getDefinitionValue(variableName);
    }

    @Override
    public String toString() {
        return "{" + variableName + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableOperand that = (VariableOperand) o;
        return Objects.equals(variableName, that.variableName);
    }
}
