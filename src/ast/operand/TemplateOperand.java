package ast.operand;

import libs.ProgramScope;
import libs.value.StringValue;
import libs.value.Value;

import java.util.List;
import java.util.Objects;

public class TemplateOperand extends Operand {
    private final List<VariableOperand> variableOperands;
    private final String template;

    /**
     *
     * @param variableOperands A variable whose value should replace the $ character in template
     * @param template A string that includes a $ character
     */
    public TemplateOperand(List<VariableOperand> variableOperands, String template) {
        this.variableOperands = variableOperands;
        this.template = template;
    }

    @Override
    public Value getValue(ProgramScope scope) {
        List<String> insertion = variableOperands.stream().map(op -> op.getValue(scope).coerceToString()).toList();
        StringBuilder resultStr = new StringBuilder();
        int i = 0;
        for (char c : template.toCharArray()) {
            if (c == '$') {
                resultStr.append(insertion.get(i));
                i++;
            } else {
                resultStr.append(c);
            }
        }
        return new StringValue(resultStr.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateOperand that = (TemplateOperand) o;
        return Objects.equals(variableOperands, that.variableOperands) && Objects.equals(template, that.template);
    }

    @Override
    public String toString() {
        return "TemplateOperand{" +
                "variableOperands=" + variableOperands +
                ", template='" + template + '\'' +
                '}';
    }
}
