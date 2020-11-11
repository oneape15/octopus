package com.oneape.octopus.commons.value;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-08 17:44.
 * Modify:
 */
public class SystemInfoUtils {

    /**
     * Get default temporary file path.
     *
     * @return String
     */
    public static String tmpDir() {
        String tmp = System.getProperties().getProperty("java.io.tmpdir");
        if (!StringUtils.endsWith(tmp, File.separator)) {
            return tmp + File.separator;
        }
        return tmp;
    }

    public static void main(String[] args) {
        System.out.print(tmpDir());
    }
}
