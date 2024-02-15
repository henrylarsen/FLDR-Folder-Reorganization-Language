package ast.folder;

import ast.operand.Operand;
import libs.ProgramScope;

import java.util.List;
import java.util.Objects;

public class ForEachFolder extends AbstractFolder {
    private final String name;
    private final List<Operand> operands;
    private final List<AbstractFolder> subfolders;

    public ForEachFolder(String name, List<Operand> operands, List<AbstractFolder> subfolders) {
        this.name = name;
        this.operands = operands;
        this.subfolders = subfolders;
    }

    @Override
    public String evaluate(ProgramScope scope) {
        String relativePathName = "";
        System.out.println("Started evaluating FOREACH with variable: " + name);
        for (Operand operand : operands) {
            scope.setLocalDefinition(name, operand.getValue(scope));
            for (AbstractFolder subfolder : subfolders) {
                String folderName = subfolder.evaluate(scope);
                if (!folderName.isEmpty()) {
                    relativePathName = folderName;
                    scope.removeLocalDefinition(name);
                    return relativePathName;
                }
            }
            // name has gone out of scope once the subfolders are done evaluating
            scope.removeLocalDefinition(name);
        }
        System.out.println("Finished evaluating FOREACH");
        return relativePathName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForEachFolder that = (ForEachFolder) o;
        return Objects.equals(name, that.name) && Objects.equals(operands, that.operands) && Objects.equals(subfolders, that.subfolders);
    }

    @Override
    public String toString() {
        return "ForEachFolder{" +
                "name='" + name + '\'' +
                ", operands=" + operands +
                ", subfolders=" + subfolders +
                '}';
    }
}
