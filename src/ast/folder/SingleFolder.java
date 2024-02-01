package ast.folder;

import ast.condition.AbstractCondition;
import ast.operand.Operand;
import libs.ProgramScope;

import java.util.List;

public class SingleFolder extends AbstractFolder {
    private Operand name;
    private AbstractCondition condition;
    private List<AbstractFolder> subfolders;

    public SingleFolder(Operand name, AbstractCondition condition, List<AbstractFolder> subfolders) {
        this.name = name;
        this.condition = condition;
        this.subfolders = subfolders;
    }


    @Override
    public void evaluate(ProgramScope scope) {
        System.out.println("Started evaluating " + name.toString());
        boolean result = condition.evaluate(scope);
        System.out.println("Condition evaluated: " + result);
        for (AbstractFolder subfolder : subfolders) {
            subfolder.evaluate(scope);
        }
        System.out.println("Finished evaluating " + name.toString());
    }
}
