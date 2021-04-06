package com.oneape.octopus.commons.dsl;

import com.oneape.octopus.commons.enums.DateType;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
}
