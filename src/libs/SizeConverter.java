package libs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeConverter {
    public static final int KB = 1024;
    public static final int MB = KB * 1024;
    public static final int GB = MB * 1024;


    /*
    Takes strings of form <numeric value> <unit>

     */
    public SizeConverter() {

    }

    /*
    Takes strings of form <numeric value> <unit>

     */
    public double convertToBytes(String s) {
        String regex = "(\\s*\\.?\\d+(.\\d+)?)\\s*(\\w+)\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format. Expected a numeric value followed by a unit.");
        }

        double size = Double.parseDouble(matcher.group(1));
        String unit = matcher.group(3).toUpperCase();

        switch (unit) {
            case "B":
                return size;
            case "KB":
                return size * KB;
            case "MB":
                return size * MB;
            case "GB":
                return size * GB;
            default:
                throw new IllegalArgumentException("Invalid unit. Expected B/KB/MB/GB.");
        }
    }

}
