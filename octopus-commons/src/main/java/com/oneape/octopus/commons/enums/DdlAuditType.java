package com.oneape.octopus.commons.enums;

import lombok.Getter;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 17:18.
 * Modify:
 */
@Getter
public enum DdlAuditType {
    SUBMIT(0, "Has been submitted"),
    AUDIT(1, "In the review"),
    EXECUTE(2, "In execution"),
    REFUSE(3, "Be rejected"),
    ERROR(4, "Perform error"),
    FINISH(5, "completes");

    private Integer code;
    private String  description;

    DdlAuditType(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
