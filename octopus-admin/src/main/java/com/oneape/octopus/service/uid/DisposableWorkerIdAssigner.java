package com.oneape.octopus.service.uid;

import com.oneape.octopus.model.enums.WorkerNodeType;
import com.oneape.octopus.commons.value.DockerUtils;
import com.oneape.octopus.commons.value.NetUtils;
import com.oneape.octopus.mapper.system.WorkerNodeMapper;
import com.oneape.octopus.model.domain.system.WorkerNodeDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {
    @Resource
    private WorkerNodeMapper workerNodeMapper;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return long  assigned worker id
     */
    @Transactional
    public long assignWorkerId() {
        WorkerNodeDO wn = buildWorkerNode();

        // add worker node for new ( ignore the same IP + PORT)
        workerNodeMapper.addWorkerNode(wn);

        log.info("Add worker node: {}", wn);
        return wn.getId();
    }

    /**
     * Build worker node entity by IP and PORT
     *
     * @return {@link WorkerNodeDO}
     */
    private WorkerNodeDO buildWorkerNode() {
        WorkerNodeDO wn;
        if (DockerUtils.isDocker()) {
            wn = new WorkerNodeDO(
                    WorkerNodeType.CONTAINER,
                    DockerUtils.getDockerHost(),
                    DockerUtils.getDockerHost()
            );
        } else {
            wn = new WorkerNodeDO(
                    WorkerNodeType.ACTUAL,
                    NetUtils.getLocalAddress(),
                    System.currentTimeMillis() + "-" + RandomUtils.nextInt(0, 100000)
            );
        }
        return wn;
    }
}
