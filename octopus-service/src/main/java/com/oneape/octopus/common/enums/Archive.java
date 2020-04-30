package com.oneape.octopus.common.enums;

public enum Archive {
    NORMAL(0), ARCHIVE(1);

    int status;

    Archive(int status) {
        this.status = status;
    }

    public int value() {
        return status;
    }
}
