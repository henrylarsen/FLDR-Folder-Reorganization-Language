package ast.condition;

import libs.ProgramScope;

public class CatchAllCondition extends AbstractCondition {
    @Override
    public boolean evaluate(ProgramScope scope) {
        // TODO: Think about how this interacts with other conditions.
        //  https://github.students.cs.ubc.ca/CPSC410-2023W-T2/Group9Project1/pull/7#discussion_r19417
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
