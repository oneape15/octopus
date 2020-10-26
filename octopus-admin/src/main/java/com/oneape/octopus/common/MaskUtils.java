package com.oneape.octopus.common;

import com.oneape.octopus.commons.value.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Permission mask manipulation tool class.
 */
public class MaskUtils {
    private static final Integer VIEW = 1;      // 1 - VIEW;
    private static final Integer ADD  = 1 << 1; // 2 - ADD;
    private static final Integer EDIT = 1 << 2; // 4 - EDIT;
    private static final Integer DEL  = 1 << 3; // 8 - DEL;


    public static List<Pair<Integer, String>> allMask() {
        List<Pair<Integer, String>> mask = new ArrayList<>();
        mask.add(new Pair<>(VIEW, "VIEW"));
        mask.add(new Pair<>(ADD, "ADD"));
        mask.add(new Pair<>(EDIT, "EDIT"));
        mask.add(new Pair<>(DEL, "DEL"));
        return mask;
    }

    /**
     * Determine if you have access to VIEW
     *
     * @param code Integer
     * @return boolean
     */
    public static boolean hasView(Integer code) {
        if (code == null) return false;
        return (code & VIEW) == VIEW;
    }

    /**
     * Determine if you have access to ADD.
     *
     * @param code Integer
     * @return boolean
     */
    public static boolean hasAdd(Integer code) {
        if (code == null) return false;
        return (code & ADD) == ADD;
    }

    /**
     * Determine if you have access to EDIT.
     *
     * @param code Integer
     * @return boolean
     */
    public static boolean hasEdit(Integer code) {
        if (code == null) return false;
        return (code & EDIT) == EDIT;
    }

    /**
     * Determine if you have access to DEL.
     *
     * @param code Integer
     * @return boolean
     */
    public static boolean hasDel(Integer code) {
        if (code == null) return false;
        return (code & DEL) == DEL;
    }

    /**
     * Add VIEW permissions
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendViewMask(Integer code) {
        if (code == null) code = 0;

        return code | VIEW;
    }

    /**
     * Add ADD permissions
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendAddMask(Integer code) {
        if (code == null) code = 0;

        return code | ADD;
    }

    /**
     * Add EDIT permissions
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendEditMask(Integer code) {
        if (code == null) code = 0;

        return code | EDIT;
    }

    /**
     * Add DEL permissions
     *
     * @param code Integer
     * @return Integer
     */
    public static Integer appendDelMask(Integer code) {
        if (code == null) code = 0;

        return code | DEL;
    }

    /**
     * All mask.
     *
     * @return Integer
     */
    public static Integer fullMask() {
        return VIEW | ADD | EDIT | DEL;
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
     * Assemble the mask value.
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
