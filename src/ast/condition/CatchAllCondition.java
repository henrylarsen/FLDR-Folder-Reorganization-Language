package ast.condition;

import libs.ProgramScope;

public class CatchAllCondition extends AbstractCondition {
    @Override
    public boolean evaluate(ProgramScope scope) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
