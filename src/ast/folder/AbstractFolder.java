package ast.folder;

import libs.Node;
import libs.ProgramScope;

public abstract class AbstractFolder extends Node {

    // Example traversal of the AST
    public abstract void evaluate(ProgramScope scope);
}
