package libs.value;

import ast.Macro;

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
}
