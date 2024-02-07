package ast;

import ast.folder.AbstractFolder;
import libs.Node;

import java.util.List;

public class Program extends Node{
    private final List<Definition> definitions;
    private final List<AbstractFolder> folders;

    public Program(List<Definition> definitions, List<AbstractFolder> folders) {
        this.definitions = definitions;
        this.folders = folders;
    }
}
