package libs.value;

import ast.Macro;

/**
 * A direct value that can be coerced to various types in the language
 */
public abstract class Value {

    public abstract Object getValue();

    public long coerceToLong() {
        throw createCannotCoerceToException("long");
    }

    public boolean coerceToBoolean() {
        throw createCannotCoerceToException("boolean");
    }

    public String coerceToString() {
        throw createCannotCoerceToException("string");
    }

    public Macro coerceToMacro() {
        throw createCannotCoerceToException("macro");
    }

    private UnsupportedOperationException createCannotCoerceToException(String type) {
        return new UnsupportedOperationException("Cannot coerce " + getClass().getSimpleName() + " to " + type);
    }
}
