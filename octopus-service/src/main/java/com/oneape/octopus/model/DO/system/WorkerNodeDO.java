package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.common.enums.WorkerNodeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class WorkerNodeDO implements Serializable {
    private Long id;
    /**
     * CONTAINER -> 主机名；ACTUAL -> IP地址
     */
    private String hostName;
    /**
     * CONTAINER -> Port, ACTUAL -> Timestamp + Random(0-10000)
     */
    private String port;

    /**
     * 工作节点类型 WorkerNodeType.CONTAINER WorkerNodeType.ACTUAL
     */
    private int type;
    /**
     * 创建时间
     */
    private Long created;
    /**
     * 最后修改时间
     */
    private Long modified;
    /**
     * 节点触发时间， 默认为当前时间
     */
    private Long launchTime = new Date().getTime();

    public WorkerNodeDO(WorkerNodeType nodeType, String hostName, String port) {
        this.type = nodeType.value();
        this.hostName = hostName;
        this.port = port;
    }
}
