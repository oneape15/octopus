package com.oneape.octopus.commons.dsl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-04-02 10:01 AM.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrossTabMacro extends Macro {
    /**
     * Each row constitutes a unique primary key.
     */
    private List<String> rowKeys;
    /**
     * Category name field name.
     */
    private String typeNameKey;
    /**
     * Category value field name.
     */
    private String typeShowKey;
    /**
     * crosstab keys
     */
    private List<String> crossKeys;

    /**
     * Get the macro type.
     *
     * @return MacroType
     */
    @Override
    public MacroType getMacroType() {
        return MacroType.CROSS_TAB;
    }

    /**
     * The request for many times.
     *
     * @return boolean
     */
    @Override
    public boolean isCompositeQuery() {
        return false;
    }

    /**
     * Add the row key
     *
     * @param key String
     * @return CrossTabMacro
     */
    public CrossTabMacro addRowKey(String key) {
        if (rowKeys == null) rowKeys = new ArrayList<>();

        rowKeys.add(key);
        return this;
    }

    /**
     * Add the cross key
     *
     * @param key String
     * @return CrossTabMacro
     */
    public CrossTabMacro addCrossKey(String key) {
        if (crossKeys == null) crossKeys = new ArrayList<>();

        crossKeys.add(key);
        return this;
    }
}
