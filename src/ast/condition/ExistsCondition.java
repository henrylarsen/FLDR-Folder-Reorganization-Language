package ast.condition;

import ast.operand.VariableOperand;
import libs.ProgramScope;

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
}
