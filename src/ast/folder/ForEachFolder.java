package ast.folder;

import libs.ProgramScope;
import libs.value.Value;

import java.util.List;

public class ForEachFolder extends AbstractFolder {
    private final String name;
    private final List<Value> values;
    private final List<AbstractFolder> subfolders;

    public ForEachFolder(String name, List<Value> values, List<AbstractFolder> subfolders) {
        this.name = name;
        this.values = values;
        this.subfolders = subfolders;
    }

    @Override
    public void evaluate(ProgramScope scope) {
        System.out.println("Started evaluating FOREACH with variable: " + name);
        for (Value value : values) {
            scope.setDefinition(name, value);
            for (AbstractFolder subfolder : subfolders) {
                subfolder.evaluate(scope);
            }
            // name has gone out of scope once the subfolders are done evaluating
            scope.removeDefinition(name);
        }
        System.out.println("Finished evaluating FOREACH");
    }
}
