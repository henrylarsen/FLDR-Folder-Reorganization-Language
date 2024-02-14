package ast.operand;

import libs.ProgramScope;
import libs.value.Value;

import java.util.Objects;

/**
 * An operand that has a constant value, regardless of the scope
 *
 * e.g. 5 or "hello"
 */
public class ConstantOperand extends Operand  {
    private final Value constantValue;

    public ConstantOperand(Value constantValue) {
        this.constantValue = constantValue;
    }

    @Override
    public Value getValue(ProgramScope scope) {
        return constantValue;
    }

    @Override
    public String toString() {
        return constantValue.coerceToString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantOperand that = (ConstantOperand) o;
        return Objects.equals(constantValue, that.constantValue);
    }
}
