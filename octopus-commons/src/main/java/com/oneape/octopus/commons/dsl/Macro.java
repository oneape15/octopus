package com.oneape.octopus.commons.dsl;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-04-02 9:58 AM.
 * Modify:
 */
public abstract class Macro {
    private MacroType macroType;

    /**
     * Get the macro type.
     *
     * @return MacroType
     */
    public abstract MacroType getMacroType();

    /**
     * The request for many times.
     *
     * @return boolean
     */
    public abstract boolean isCompositeQuery();

}
