package com.oneape.octopus.common;


import com.oneape.octopus.commons.value.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限掩码操作工具类
 */
public class MaskUtils {
    private static final Integer VIEW = 1;      // 1 - 查看;
    private static final Integer ADD  = 1 << 1; // 2 - 新增;
    private static final Integer EDIT = 1 << 2; // 4 - 修改;
    private static final Integer DEL  = 1 << 3; // 8 - 删除;


    public static List<Pair<Integer, String>> allMask() {
        List<Pair<Integer, String>> mask = new ArrayList<>();
        mask.add(new Pair<>(VIEW, "查看"));
        mask.add(new Pair<>(ADD, "新增"));
        mask.add(new Pair<>(EDIT, "修改"));
        mask.add(new Pair<>(DEL, "删除"));
        return mask;
    }

    /**
     * 判断是否有查看权限
     *
     * @param code Integer
     * @return boolean true - 有; false -无
     */
    public static boolean hasView(Integer code) {
        if (code == null) return false;
        return (code & VIEW) == VIEW;
    }

    /**
     * 判断是否有新增权限
     *
     * @param code Integer
     * @return boolean true - 有; false -无
     */
    public static boolean hasAdd(Integer code) {
        if (code == null) return false;
        return (code & ADD) == ADD;
    }

    /**
     * 判断是否有修改权限
     *
     * @param code Integer
     * @return boolean true - 有; false -无
     */
    public static boolean hasEdit(Integer code) {
        if (code == null) return false;
        return (code & EDIT) == EDIT;
    }

    /**
     * 判断是否有删除权限
     *
     * @param code Integer
     * @return boolean true - 有; false -无
     */
    public static boolean hasDel(Integer code) {
        if (code == null) return false;
        return (code & DEL) == DEL;
    }

    /**
     * 添加查看权限
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendViewMask(Integer code) {
        if (code == null) code = 0;

        return code | VIEW;
    }

    /**
     * 添加新增权限
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendAddMask(Integer code) {
        if (code == null) code = 0;

        return code | ADD;
    }

    /**
     * 添加修改权限
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendEditMask(Integer code) {
        if (code == null) code = 0;

        return code | EDIT;
    }

    /**
     * 全掩码
     *
     * @return Integer
     */
    public static Integer fullMask() {
        return VIEW | ADD | EDIT | DEL;
    }

    /**
     * 添加删除权限
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendDelMask(Integer code) {
        if (code == null) code = 0;

        return code | DEL;
    }

    public static List<Integer> getList(Integer mask) {
        List<Integer> arr = new ArrayList<>();
        if (hasView(mask)) arr.add(VIEW);
        if (hasAdd(mask)) arr.add(ADD);
        if (hasEdit(mask)) arr.add(EDIT);
        if (hasDel(mask)) arr.add(DEL);
        return arr;
    }

    public static List<Integer> getAllList() {
        List<Integer> arr = new ArrayList<>();
        arr.add(VIEW);
        arr.add(ADD);
        arr.add(EDIT);
        arr.add(DEL);

        return arr;
    }

    /**
     * 组装mask值
     *
     * @param masks List
     * @return Integer
     */
    public static Integer getMask(List<Integer> masks) {
        if (masks == null || masks.size() == 0) return 0;

        int mask = 0;
        for (Integer m : masks) {
            mask |= m;
        }

        return mask;
    }
}
