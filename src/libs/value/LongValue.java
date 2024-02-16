package libs.value;

public class LongValue extends Value {
    private final long value;

    public LongValue(long value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public long coerceToLong() {
        return value;
    }

    @Override
    public String coerceToString() {
        return Long.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongValue that = (LongValue) o;
        return value == that.value;
    }

    @Override
    public String toString() {
        return "IntegerValue{" +
                "value=" + value +
                '}';
    }
}
