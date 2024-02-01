package libs;

import libs.value.Value;

import java.util.HashMap;
import java.util.Map;

public class ProgramScope {
    private final Map<String, Value> currentScope = new HashMap<>();

    public ProgramScope() {
    }

    ProgramScope(ProgramScope parent) {
        this.currentScope.putAll(parent.currentScope);
    }

    public Value getDefinitionValue(String name) {
        if (!currentScope.containsKey(name)) {
            throw new NullPointerException("No definition exists by the name of " + name);
        }

        return currentScope.get(name);
    }

    public boolean hasDefinition(String name) {
        return currentScope.containsKey(name);
    }

    public void setDefinition(String name, Value value) {
        if (currentScope.containsKey(name)) {
            throw new IllegalArgumentException("Definition " + name + " is already defined!");
        }

        currentScope.put(name, value);
    }

    public ProgramScope copy() {
        return new ProgramScope(this);
    }
}
