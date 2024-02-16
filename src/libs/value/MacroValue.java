package libs.value;

import ast.Macro;

import java.util.Objects;

public class MacroValue extends Value {
    private final Macro value;

    public MacroValue(Macro value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Macro coerceToMacro() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MacroValue that = (MacroValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "MacroValue{" +
                "value=" + value +
                '}';
    }
}
