package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.model.enums.WorkerNodeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * UID generator DO
 */
@Data
@NoArgsConstructor
public class WorkerNodeDO implements Serializable {
    private Long   id;
    /**
     * CONTAINER -> host name；
     * ACTUAL -> ip address.
     */
    private String hostName;
    /**
     * CONTAINER -> Port, ACTUAL -> Timestamp + Random(0-10000)
     */
    private String port;

    /**
     * node type WorkerNodeType.CONTAINER WorkerNodeType.ACTUAL
     */
    private int  type;
    /**
     * create time
     */
    private Long created;
    /**
     * Last updated time
     */
    private Long modified;
    /**
     * Node trigger time, default to current time
     */
    private Long launchTime = new Date().getTime();

    public WorkerNodeDO(WorkerNodeType nodeType, String hostName, String port) {
        this.type = nodeType.value();
        this.hostName = hostName;
        this.port = port;
    }
}
