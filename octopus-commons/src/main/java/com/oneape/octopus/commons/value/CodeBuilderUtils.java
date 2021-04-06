package com.oneape.octopus.commons.value;

import java.util.Random;

public class CodeBuilderUtils {
    private static final char[] ALL_CHAR = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    private static final int ALL_CHAR_LEN = ALL_CHAR.length;

    private static final Random random = new Random();

    /**
     * Gets a random string of the specified length.
     *
     * @param len int
     * @return String
     */
    public static String randomStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(ALL_CHAR[random.nextInt(ALL_CHAR_LEN)]);
        }
        return sb.toString();
    }
}
