package ast.condition.comparison.string;

import java.util.function.BiFunction;

public enum StringComparisonType {
    CONTAINS(String::contains),
    MATCHES(String::matches),
    STARTS_WITH(String::startsWith),
    ENDS_WITH(String::endsWith),
    IS_IGNORE_CASE(String::equalsIgnoreCase);

    private BiFunction<String, String, Boolean> comparisonFunction;

    StringComparisonType(BiFunction<String, String, Boolean> comparisonFunction) {
        this.comparisonFunction = comparisonFunction;
    }

    public boolean compare(String leftString, String rightString) {
        return this.comparisonFunction.apply(leftString, rightString);
    }
}
