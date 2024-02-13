package ast.operand;

import libs.ProgramScope;
import libs.value.StringValue;
import libs.value.Value;

import java.util.Objects;

// TODO: Currently this allows for a single variable in a single template string
//  Consider if this is the best way to do template strings / variable replacement
public class TemplateOperand extends Operand {
    private final VariableOperand variableOperand;
    private final String template;

    /**
     *
     * @param variableOperand A variable whose value should replace the $ character in template
     * @param template A string that includes a $ character
     */
    public TemplateOperand(VariableOperand variableOperand, String template) {
        this.variableOperand = variableOperand;
        this.template = template;
    }

    @Override
    public Value getValue(ProgramScope scope) {
        String insertion = variableOperand.getValue(scope).coerceToString();
        return new StringValue(template.replace("$", insertion));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateOperand that = (TemplateOperand) o;
        return Objects.equals(variableOperand, that.variableOperand) && Objects.equals(template, that.template);
    }
}
