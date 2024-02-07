package ast.condition.junction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum ConditionJunctionType {
    AND((a, b) -> a.get() && b.get()),
    OR((a, b) -> a.get() || b.get());

    // use bifunction to easily define junctions as lambdas
    // suppliers are also used instead of booleans directly so that short-circuiting works as expected
    private BiFunction<Supplier<Boolean>, Supplier<Boolean>, Boolean> junctionFunction;

    ConditionJunctionType(BiFunction<Supplier<Boolean>, Supplier<Boolean>, Boolean> junctionFunction) {
        this.junctionFunction = junctionFunction;
    }

    boolean join(Supplier<Boolean> first, Supplier<Boolean> second) {
        return junctionFunction.apply(first, second);
    }
}
