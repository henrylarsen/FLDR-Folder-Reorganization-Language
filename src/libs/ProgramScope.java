package libs;

import libs.value.Value;

import java.util.HashMap;
import java.util.Map;

public class ProgramScope {
    /**
     * Global scope is for definitions that span the whole program, specifically for:
     * - Macros
     * - File related definitions
     */
    private final Map<String, Value> globalScope = new HashMap<>();
    private final Map<String, Value> localScope = new HashMap<>();

    public ProgramScope() {
    }

    ProgramScope(ProgramScope parent) {
        this.globalScope.putAll(parent.globalScope);
    }

    public Value getDefinitionValue(String name) {
        if (localScope.containsKey(name)) {
            return localScope.get(name);
        }

        if (globalScope.containsKey(name)) {
            return globalScope.get(name);
        }

        System.out.println("local: " + localScope);
        System.out.println("global: " + globalScope);
        throw new NullPointerException("No definition exists by the name of " + name);
    }

    public boolean hasDefinition(String name) {
        return localScope.containsKey(name) || globalScope.containsKey(name);
    }

    public void setLocalDefinition(String name, Value value) {
        if (hasDefinition(name)) {
            throw new IllegalArgumentException("Definition " + name + " is already defined!");
        }

        localScope.put(name, value);
    }

    /**
     * Sets a global definition that will get passed onto the nested scopes
     */
    public void setGlobalDefinition(String name, Value value) {
        if (hasDefinition(name)) {
            throw new IllegalArgumentException("Definition " + name + " is already defined!");
        }

        globalScope.put(name, value);
    }

    /**
     * Builds a new scope with the same global variables
     */
    public ProgramScope buildNew() {
        return new ProgramScope(this);
    }

    public void removeLocalDefinition(String name) {
        if (!localScope.containsKey(name)) {
            throw new IllegalArgumentException("Definition " + name + " is not defined in local scope!");
        }

        localScope.remove(name);
    }
}
