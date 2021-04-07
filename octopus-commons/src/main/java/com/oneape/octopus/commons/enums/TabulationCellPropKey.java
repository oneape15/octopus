package com.oneape.octopus.commons.enums;

/**
 * Table item related properties.
 * Created by oneape<oneape15@163.com>
 * Created 2021-04-06 5:19 PM.
 * Modify:
 */
public enum TabulationCellPropKey {
    // year on year value
    yoy("yoy"),
    // chain rate value
    rate("rate"),
    // cell color for display.
    color("color");

    private final String code;

    private TabulationCellPropKey(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
