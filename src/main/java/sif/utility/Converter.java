package sif.utility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    private static final String WORKSHEET_REGEX = "([\\s\\w]+)!";
    private static final String COLUMN_REGEX = "[\\$]?([A-Z]+)";
    private static final String ROW_REGEX = "[\\$]?([0-9]+)";
    private static final String SIMPLE_NOTATION_REGEX = "^" + COLUMN_REGEX + ROW_REGEX + "$";
    private static final String EXCEL_NOTATION_REGEX = "^" + WORKSHEET_REGEX + COLUMN_REGEX + ROW_REGEX + "$";
    public static final String SIMPLE_MATRIX_NOTATION_REGEX = "^" + COLUMN_REGEX + ROW_REGEX + ":" + COLUMN_REGEX + ROW_REGEX + "$";
    public static final String EXCEL_MATRIX_NOTATION_REGEX = "^" + WORKSHEET_REGEX + COLUMN_REGEX + ROW_REGEX + ":" + COLUMN_REGEX + ROW_REGEX + "$";

    @Nullable
    public static List<String> extractExcelNotation(String s) {
        Pattern pattern = Pattern.compile(Converter.EXCEL_NOTATION_REGEX);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find() && (matcher.groupCount() == 3)) {
            List<String> list = new ArrayList<>();
            list.add(matcher.group(1));
            list.add(matcher.group(2));
            list.add(matcher.group(3));
            return list;
        } else {
            return null;
        }
    }

    @Nullable
    public static List<String> extractSimpleNotation(String s) {
        Pattern pattern = Pattern.compile(Converter.SIMPLE_NOTATION_REGEX);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find() && (matcher.groupCount() == 2)) {
            List<String> list = new ArrayList<>();
            list.add(matcher.group(1));
            list.add(matcher.group(2));
            return list;
        } else {
            return null;
        }
    }

    public static int columnToInteger(String name) {
        int number = 0;
        for (int i = 0; i < name.length(); i++) {
            number = (number * 26) + (name.charAt(i) - ('A' - 1));
        }
        return number;
    }

    public static String columnToString(int number) {
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            number = number - 1;
            sb.append((char) ('A' + (number % 26)));
            number = number / 26;
        }
        return sb.reverse().toString();
    }
}
