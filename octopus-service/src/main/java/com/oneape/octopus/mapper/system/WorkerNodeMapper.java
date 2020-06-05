package com.oneape.octopus.mapper.system;

import com.oneape.octopus.model.DO.system.WorkerNodeDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface WorkerNodeMapper {

    String TB_WORKER_NODE = "sys_worker_node";

    /**
     * Get {@link WorkerNodeDO} by node host and port
     *
     * @param host String node host
     * @param port String node port
     * @return WorkerNode
     */
    @Select({
            "SELECT * FROM " + TB_WORKER_NODE + " WHERE host_name = #{hostName} AND port = #{port}"
    })
    WorkerNodeDO getByHostPort(@Param("host") String host, @Param("port") String port);

    /**
     * Add {@link WorkerNodeDO}
     *
     * @param workerNode WorkerNode
     * @return int 0 - fail, 1 - success
     */
    @Insert({
            "INSERT INTO " + TB_WORKER_NODE +
                    "( host_name, port, type, launch_time, created, modified " +
                    ") VALUES (" +
                    "  #{hostName}," +
                    "  #{port}," +
                    "  #{type}," +
                    "  #{launchTime}," +
                    "  now()," +
                    "  now()" +
                    ")"
    })
    @SelectKey(
            statement = "select IFNULL(max(id), 1) from " + TB_WORKER_NODE,
            keyProperty = "id",
            before = false,
            resultType = Long.class)
    int addWorkerNode(WorkerNodeDO workerNode);

}
