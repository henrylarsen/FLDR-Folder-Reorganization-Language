package ast.operand;

import libs.Node;
import libs.ProgramScope;
import libs.value.Value;

/**
 * Representation of some value that can be operated on
 */
public abstract class Operand extends Node {
    abstract public Value getValue(ProgramScope scope);
}
