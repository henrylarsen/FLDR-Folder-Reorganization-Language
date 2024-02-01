package ast.operand;

import libs.ProgramScope;
import libs.value.Value;

/**
 * An operand referring to a variable
 */
public class VariableOperand implements Operand {
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
}
