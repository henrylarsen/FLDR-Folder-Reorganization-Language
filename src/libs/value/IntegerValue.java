package libs.value;

public class IntegerValue extends Value {
    private final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int coerceToInt() {
        return value;
    }

    @Override
    public String coerceToString() {
        return Integer.toString(value);
    }
}
