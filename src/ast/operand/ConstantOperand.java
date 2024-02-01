package ast.operand;

import libs.ProgramScope;
import libs.value.Value;

/**
 * An operand that has a constant value, regardless of the scope
 *
 * e.g. 5 or "hello"
 */
public class ConstantOperand implements Operand {
    private final Value constantValue;

    public ConstantOperand(Value constantValue) {
        this.constantValue = constantValue;
    }

    @Override
    public Value getValue(ProgramScope scope) {
        return constantValue;
    }
}
