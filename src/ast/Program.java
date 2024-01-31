package ast;

import libs.Node;
import java.util.List;

public class Program extends Node{
    private final List<Definition> definitions;
    // TODO: The rest of the owl

    public Program(List<Definition> definitions) {
        this.definitions = definitions;
    }
}
