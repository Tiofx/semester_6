package lab2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static lab2.util.variantNative.Code.*;
import static lab2.util.variantNative.Code.Error;

public class Parser {

    public static void toText(String filename) {
        HashMap<Integer, String> replaceStrings = new HashMap<Integer, String>() {{
            put(PROC, "PROC");
            put(END, "END");
            put(IDEN, "IDEN");
            put(DATA, "DATA");
            put(END, "END");

            put(Sign.EQUALLY, "Sign.EQUALLY");
            put(Sign.COMMA, "Sign.COMMA");
            put(Sign.DOUBLE_AMPERSAND, "Sign.DOUBLE_AMPERSAND");
            put(Sign.DOUBLE_EXCLAMATION_POINT, "Sign.DOUBLE_EXCLAMATION_POINT");
            put(Sign.LEFT_SHIFT, "Sign.LEFT_SHIFT");
            put(Sign.RIGHT_SHIFT, "Sign.RIGHT_SHIFT");

            put(Error.L3, "Error.L3RowNumber");
            put(Error.IN_CHAIN, "Error.IN_CHAIN");
            put(Error.IN_KEY_WORD, "Error.IN_KEY_WORD");
            put(Error.IN_VARIABLE, "Error.IN_VARIABLE");
            put(Error.IN_CONSTANT, "Error.IN_CONSTANT");
            put(Error.IN_DOUBLE_AMPERSAND, "Error.IN_DOUBLE_AMPERSAND");
            put(Error.IN_DOUBLE_EXCLAMATION_POINT, "Error.IN_DOUBLE_EXCLAMATION_POINT");
            put(Error.IN_LEFT_SHIFT, "Error.IN_LEFT_SHIFT");
            put(Error.IN_RIGHT_SHIFT, "Error.IN_RIGHT_SHIFT");
        }};

        try {
            new BufferedReader(new FileReader(new File(filename)))
                    .lines()
                    .map(s -> "{" + s.replaceAll("\t", ", ") + "},")
                    .map(s -> s.replace(", }", " }"))
                    .map(s -> {
                                for (Map.Entry<Integer, String> map : replaceStrings.entrySet()) {
                                    s = s.replaceAll(Integer.toString(map.getKey()), map.getValue());
                                }
                                return s;
                            }
                    )
                    .forEach(System.out::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static int[][] toArray(String filename) {
        try {
            return new BufferedReader(new FileReader(new File(filename)))
                    .lines()
                    .map(s -> s.split("\t"))
                    .map(s -> Arrays.stream(s)
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .mapToInt(i -> i)
                            .toArray()
                    )
                    .toArray(int[][]::new);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "table.txt";

        for (int i = 0; i < 10; i++) {
            System.out.println((Integer) i);
        }

//        Arrays.stream(toArray(filename))
//                .forEachOrdered(ints -> {
//                    Arrays.stream(ints)
//                            .mapToObj(String::valueOf)
//                            .map(s -> s + "\t")
//                            .forEachOrdered(System.out::printf);
//                    System.out.println();
//                });
    }
}
