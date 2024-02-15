package ast.folder;

import libs.Node;
import libs.ProgramScope;

public abstract class AbstractFolder extends Node {

    // Example traversal of the AST
    // return relative path name in if folder matches file evaluation, else return empty string ""
    public abstract String evaluate(ProgramScope scope);
}
