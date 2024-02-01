package libs.value;

public class StringValue extends Value {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int coerceToInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new UnsupportedOperationException("Cannot coerce value '" + value + "' into an integer");
        }
    }

    @Override
    public boolean coerceToBoolean() {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t")) {
            return true;
        }

        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("f")) {
            return false;
        }

        throw new UnsupportedOperationException("Cannot coerce value '" + value + "' into a boolean");
    }

    @Override
    public String coerceToString() {
        return value;
    }
}
