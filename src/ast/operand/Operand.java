package ast.operand;

import libs.ProgramScope;
import libs.value.Value;

/**
 * Representation of some value that can be operated on
 */
public interface Operand {
    Value getValue(ProgramScope scope);
}
