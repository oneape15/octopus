package com.oneape.octopus.commons.dsl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-04-02 10:44 AM.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TotalMacro extends Macro {
    /**
     * The total row insert location.
     * 0 - the first;
     * 1 - the end;
     */
    private Integer insertType;
    /**
     * The field that holds the word total.
     */
    private String tagField;

    /**
     * A list of fields to calculate the total.
     */
    private List<String> sumFiledList;

    /**
     * Get the macro type.
     *
     * @return MacroType
     */
    @Override
    public MacroType getMacroType() {
        return MacroType.TOTAL;
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
}
