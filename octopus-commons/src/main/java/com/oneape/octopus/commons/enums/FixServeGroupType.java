package com.oneape.octopus.commons.enums;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-31 17:30.
 * Modify:
 */
public enum FixServeGroupType {
    ROOT(0L, "ROOT"),
    PERSONAL(-1L, "PERSONAL"),
    ARCHIVE(-2L, "ARCHIVE");

    private Long   id;
    private String groupName;

    FixServeGroupType(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }
}
