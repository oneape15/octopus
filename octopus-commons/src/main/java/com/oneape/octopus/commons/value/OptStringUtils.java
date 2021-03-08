package com.oneape.octopus.commons.value;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-24 10:53.
 * Modify:
 */
public final class OptStringUtils {

    public static List<String> split(String str, String splitChar) {
        List<String> list = new ArrayList<>();
        String[] arr = StringUtils.split(str, splitChar);
        if (arr != null && arr.length > 0) {
            list = Arrays.asList(arr);
        }
        return list;
    }

    public static String iterator2String(Iterator<String> iter) {
        StringBuilder s = new StringBuilder("[");
        int index = 0;
        while (iter.hasNext()) {
            if (index++ > 0) {
                s.append(",");
            }
            s.append(iter.next());
        }
        s.append("]");

        return s.toString();
    }
}
