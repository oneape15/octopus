package com.oneape.octopus.service.serve.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.commons.algorithm.Digraph;
import com.oneape.octopus.commons.algorithm.DirectedCycle;
import com.oneape.octopus.commons.value.OptStringUtils;
import com.oneape.octopus.mapper.serve.ServeInfoMapper;
import com.oneape.octopus.model.DO.serve.ServeInfoDO;
import com.oneape.octopus.model.DTO.serve.ServeParamDTO;
import com.oneape.octopus.service.serve.ServeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class ServeInfoServiceImpl implements ServeInfoService {
    @Resource
    private ServeInfoMapper serveInfoMapper;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(ServeInfoDO model) {
        Preconditions.checkNotNull(model, "The report object is null.");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "The report name is empty.");
        if (model.getId() != null && model.getId() > 0) {
            boolean valid = checkReportId(model.getId());
            if (!valid) {
                throw new BizException("Invalid report id");
            }
            return serveInfoMapper.update(model);
        }

        return serveInfoMapper.insert(model);
    }

    /**
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(ServeInfoDO model) {
        return 0;
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    @Transactional
    public int deleteById(Long id) {
        Preconditions.checkNotNull(id, "The report primary key Id is empty.");

        return serveInfoMapper.delete(new ServeInfoDO(id));
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public ServeInfoDO findById(Long id) {
        return serveInfoMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param model ServeInfoDO
     * @return List
     */
    @Override
    public List<ServeInfoDO> find(ServeInfoDO model) {
        Preconditions.checkNotNull(model, "The report object is null.");
        return serveInfoMapper.list(model);
    }

    /**
     * Whether the report Id is valid.
     *
     * @param reportId Long
     * @return boolean true - valid. false - invalid.
     */
    @Override
    public boolean checkReportId(Long reportId) {
        int size = serveInfoMapper.checkReportId(reportId);
        return size > 0;
    }

    /**
     * Judge the correctness of the serve query parameter information.
     *
     * @param params List
     */
    @Override
    public void checkServeParams(List<ServeParamDTO> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }

        // Whether an argument with the same name exists.
        List<String> names = new ArrayList<>();
        for (ServeParamDTO p : params) {
            Preconditions.checkArgument(StringUtils.isNotBlank(p.getName()), "The report param name is empty.");
            Preconditions.checkArgument(StringUtils.isNotBlank(p.getDataType()), "The report param dataType is empty.");
            Preconditions.checkArgument(p.getType() != null, "The report param type is empty.");

            if (names.contains(p.getName())) {
                throw new BizException("There are multiple parameters named: " + p.getName());
            } else {
                names.add(p.getName());
            }
        }

        // Check whether parameter dependencies form loops.
        //
        // Determine if there are rings in a linked list.
        //  5 --> 3 --> 7 ---> 2 --> 6 -- 8 --> 1 --> 2  , The linked is exist loop.
        Digraph<String> digraph = buildDigraph(params);

        DirectedCycle<String> directedCycle = new DirectedCycle<>(digraph);
        if (directedCycle.hasCycle()) {
            String s = OptStringUtils.iterator2String(directedCycle.cycle());
            throw new BizException("Parameters are interdependent: " + s);
        }
    }

    /**
     * Build a directed graph of parameter dependencies.
     *
     * @param paramDOs List
     * @return Digraph
     */
    private Digraph<String> buildDigraph(List<ServeParamDTO> paramDOs) {

        // return a empty digraph.
        if (CollectionUtils.isEmpty(paramDOs)) {
            return new Digraph<>(0);
        }

        Map<String, List<String>> dependMap = new HashMap<>();
        HashSet<String> allNodes = new HashSet<>();

        // Build a directed graph.
        for (ServeParamDTO p : paramDOs) {
            if (StringUtils.isBlank(p.getDependOn())) {
                continue;
            }
            String[] arr = StringUtils.split(p.getDependOn(), ";");
            if (arr != null && arr.length > 0) {
                List<String> dependList = new ArrayList<>();
                for (String s : arr) {
                    dependList.add(s);
                }
                allNodes.addAll(dependList);
                allNodes.add(p.getName());
                dependMap.put(p.getName(), dependList);
            }
        }

        Digraph<String> digraph = new Digraph<>(allNodes.size());
        dependMap.forEach((k, v) -> {
            for (String tmp : v) {
                digraph.addEdge(k, tmp);
            }
        });

        return digraph;
    }

}
