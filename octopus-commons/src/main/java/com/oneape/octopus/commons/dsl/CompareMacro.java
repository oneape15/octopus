package com.oneape.octopus.commons.dsl;

import com.oneape.octopus.commons.enums.DateType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-04-02 10:40 AM.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompareMacro extends Macro {
    /**
     * Compare date type.
     */
    private DateType dateType;
    /**
     * The date format.
     */
    private String dateFormat;
    /**
     * The date param.
     */
    private String dateParam;
    /**
     * Determine the row's unique primary key.
     */
    private List<String> rowKeys;
    /**
     * The need compare filed list.
     */
    private List<String> compareFiledList;
    /**
     * Fields that do not need to be compared.
     */
    private List<String> compareFilterFiledList;

    /**
     * Get the macro type.
     *
     * @return MacroType
     */
    @Override
    public MacroType getMacroType() {
        return MacroType.COMPARE;
    }

    /**
     * The request for many times.
     *
     * @return boolean
     */
    @Override
    public boolean isCompositeQuery() {
        return true;
    }

    /**
     * Add the row key
     *
     * @param key String
     * @return CompareMacro
     */
    public CompareMacro addRowKey(String key) {
        if (rowKeys == null) rowKeys = new ArrayList<>();

        rowKeys.add(key);
        return this;
    }

    /**
     * Add the compare key
     *
     * @param key String
     * @return CompareMacro
     */
    public CompareMacro addCompareKey(String key) {
        if (compareFilterFiledList == null) compareFilterFiledList = new ArrayList<>();

        compareFilterFiledList.add(key);
        return this;
    }

    /**
     * Add the filter key
     *
     * @param key String
     * @return CompareMacro
     */
    public CompareMacro addFilterKey(String key) {
        if (compareFiledList == null) compareFiledList = new ArrayList<>();

        compareFiledList.add(key);
        return this;
    }
}
