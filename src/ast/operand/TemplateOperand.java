package ast.operand;

import libs.ProgramScope;
import libs.value.StringValue;
import libs.value.Value;

// TODO: Currently this allows for a single variable in a single template string
//  Consider if this is the best way to do template strings / variable replacement
public class TemplateOperand implements Operand {
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
}
