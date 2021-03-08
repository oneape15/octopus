package com.oneape.octopus.commons.enums;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-31 17:30.
 * Modify:
 */
public enum FixServeGroupType {
    ROOT(0L, "根目录"),
    PERSONAL(-1L, "个人文件夹"),
    ARCHIVE(-2L, "已归档目录");

    private final Long   id;
    private final String groupName;

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
