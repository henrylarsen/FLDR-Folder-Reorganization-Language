package ast.folder;

import libs.ProgramScope;
import libs.value.Value;

import java.util.List;
import java.util.Objects;

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
            scope.setLocalDefinition(name, value);
            for (AbstractFolder subfolder : subfolders) {
                subfolder.evaluate(scope);
            }
            // name has gone out of scope once the subfolders are done evaluating
            scope.removeLocalDefinition(name);
        }
        System.out.println("Finished evaluating FOREACH");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForEachFolder that = (ForEachFolder) o;
        return Objects.equals(name, that.name) && Objects.equals(values, that.values) && Objects.equals(subfolders, that.subfolders);
    }
}
