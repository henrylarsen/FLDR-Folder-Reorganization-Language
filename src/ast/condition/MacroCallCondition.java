package ast.condition;

import ast.Macro;
import ast.operand.Operand;
import libs.ProgramScope;
import libs.value.Value;

import java.util.List;
import java.util.Objects;

/**
 * A call to a macro function that will evaluate to a boolean
 *
 * Parameter match checked at evaluation.
 */
public class MacroCallCondition extends AbstractCondition {
    private final String macroName;
    private final List<Operand> parameterValues;

    public MacroCallCondition(String macroName, List<Operand> parameterValues) {
        this.macroName = macroName;
        this.parameterValues = parameterValues;
    }

    @Override
    public boolean evaluate(ProgramScope scope) {
        Macro macro = scope.getDefinitionValue(macroName).coerceToMacro();
        List<String> macroParameters = macro.getParameters();
        ProgramScope newScope = scope.buildNew();

        if (parameterValues.size() != macroParameters.size()) {
            throw new IllegalStateException("Mismatched parameters for macro " + macroName);
        }

        // update scope with new definitions
        for (int i = 0; i < macroParameters.size(); i++) {
            String parameterName = macroParameters.get(i);
            Value parameterValue = parameterValues.get(i).getValue(scope);

            newScope.setLocalDefinition(parameterName, parameterValue);
        }

        return macro.evaluate(newScope);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MacroCallCondition that = (MacroCallCondition) o;
        return Objects.equals(macroName, that.macroName) && Objects.equals(parameterValues, that.parameterValues);
    }

    @Override
    public String toString() {
        return "MacroCallCondition{" +
                "macroName='" + macroName + '\'' +
                ", parameterValues=" + parameterValues +
                '}';
    }
}
