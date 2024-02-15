package libs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SizeConverter {
    private static final int KB = 1024;
    private static final int MB = KB * 1024;
    private static final int GB = MB * 1024;
    private SizeConverter(){}

    /*
        Takes strings of form <numeric value><unit>
        and returns the quantity in bytes
     */
    public static long convertToBytes(String s) {
        String regex = "(\\s*\\.?\\d+(.\\d+)?)(\\w+)\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format. Expected a numeric value directly followed by a unit.");
        }

        // allows for decimals in user input
        double size = Double.parseDouble(matcher.group(1));
        String unit = matcher.group(3).toUpperCase();

        switch (unit) {
            case "B":
                break;
            case "KB":
                size = size * KB;
                break;
            case "MB":
                size = size * MB;
                break;
            case "GB":
                size = size * GB;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit. Expected B/KB/MB/GB.");
        }

        // rounded down to a whole number
        return (long)size;

    }

}
