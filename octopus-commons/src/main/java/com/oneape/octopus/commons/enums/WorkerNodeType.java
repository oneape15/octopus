package com.oneape.octopus.commons.enums;

public enum WorkerNodeType {
    // container. eg: Docker
    CONTAINER(1),
    // Real physical machine.
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
