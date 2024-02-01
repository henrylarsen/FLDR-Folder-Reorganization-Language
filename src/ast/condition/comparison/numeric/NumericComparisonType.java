package ast.condition.comparison.numeric;

import java.util.function.BiFunction;

public enum NumericComparisonType {
    GREATER_THAN((a, b) -> a > b),
    GREATER_THAN_EQUAL((a, b) -> a >= b),
    LESS_THAN((a, b) -> a < b),
    LESS_THAN_EQUAL((a, b) -> a <= b);

    private BiFunction<Integer, Integer, Boolean> comparisonFunction;

    NumericComparisonType(BiFunction<Integer, Integer, Boolean> comparisonFunction) {
        this.comparisonFunction = comparisonFunction;
    }

    public boolean compare(int leftValue, int rightValue) {
        return this.comparisonFunction.apply(leftValue, rightValue);
    }
}
