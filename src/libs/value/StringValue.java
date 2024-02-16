package libs.value;

import java.util.Objects;

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
    public long coerceToLong() {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new UnsupportedOperationException("Cannot coerce value '" + value + "' into a long");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringValue that = (StringValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "StringValue{" +
                "value='" + value + '\'' +
                '}';
    }
}
