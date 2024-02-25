package ast.condition.comparison.numeric;

import java.util.function.BiFunction;

public enum NumericComparisonType {
    GREATER_THAN((a, b) -> a > b),
    GREATER_THAN_EQUAL((a, b) -> a >= b),
    LESS_THAN((a, b) -> a < b),
    LESS_THAN_EQUAL((a, b) -> a <= b),
    EQUAL_TO((a,b) -> (long) a == (long) b);

    private BiFunction<Long, Long, Boolean> comparisonFunction;

    NumericComparisonType(BiFunction<Long, Long, Boolean> comparisonFunction) {
        this.comparisonFunction = comparisonFunction;
    }

    public boolean compare(long leftValue, long rightValue) {
        return this.comparisonFunction.apply(leftValue, rightValue);
    }
}
