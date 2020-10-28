package com.oneape.octopus.commons.constant;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-04-30 17:36.
 * Modify:
 */
public class OctopusConstant {

    // The password mask
    public final static String PWD_MASK_TAG = "<masked>";
    /**
     * The system user id.
     */
    public final static Long   SYS_USER     = -1L;

    /**
     * The default value.
     */
    public final static Long DEFAULT_VALUE = 0L;

    public final static Integer YES = 1;
    public final static Integer NO  = 0;

    public final static Integer SUCCESS = 1;
    public final static Integer FAIL    = 0;

    public static boolean isSuccess(Integer status) {
        return status != null && SUCCESS.equals(status);
    }
}
