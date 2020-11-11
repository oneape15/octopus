package com.oneape.octopus.commons.enums;

import lombok.Getter;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-08 18:07.
 * Modify:
 */
@Getter
public enum FileType {
    CSV(".csv"),
    XLS(".xlsx");

    private String suffix;

    private FileType(String suffix) {
        this.suffix = suffix;
    }
}
