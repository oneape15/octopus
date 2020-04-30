package com.oneape.octopus.common.enums;

public enum WorkerNodeType {
    // 容器模式, 如 Docker
    CONTAINER(1),
    // 真实物理机
    ACTUAL(2);

    /**
     * Lock type
     */
    private final Integer type;

    WorkerNodeType(Integer type) {
        this.type = type;
    }

    public Integer value() {
        return type;
    }
}
