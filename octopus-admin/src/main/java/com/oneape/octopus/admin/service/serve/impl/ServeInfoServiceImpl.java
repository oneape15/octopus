package com.oneape.octopus.admin.service.serve.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.config.SessionThreadLocal;
import com.oneape.octopus.admin.service.system.AccountService;
import com.oneape.octopus.commons.algorithm.Digraph;
import com.oneape.octopus.commons.algorithm.DirectedCycle;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.enums.ServeStatusType;
import com.oneape.octopus.commons.enums.VersionType;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.commons.value.OptStringUtils;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.domain.serve.ServeRlGroupDO;
import com.oneape.octopus.domain.serve.ServeVersionDO;
import com.oneape.octopus.dto.serve.ServeColumnDTO;
import com.oneape.octopus.dto.serve.ServeConfigTextDTO;
import com.oneape.octopus.dto.serve.ServeParamDTO;
import com.oneape.octopus.dto.serve.ServeSqlDTO;
import com.oneape.octopus.mapper.serve.ServeGroupMapper;
import com.oneape.octopus.mapper.serve.ServeInfoMapper;
import com.oneape.octopus.mapper.serve.ServeRlGroupMapper;
import com.oneape.octopus.mapper.serve.ServeVersionMapper;
import com.oneape.octopus.admin.service.serve.ServeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ServeInfoServiceImpl implements ServeInfoService {
    @Resource
    private ServeInfoMapper serveInfoMapper;
    @Resource
    private ServeGroupMapper serveGroupMapper;
    @Resource
    private ServeRlGroupMapper serveRlGroupMapper;
    @Resource
    private ServeVersionMapper serveVersionMapper;
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private AccountService accountService;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int save(ServeInfoDO model) {
        throw new RuntimeException(I18nMsgConfig.getMessage("global.unrealized"));
    }

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model   ServeInfoDO
     * @param groupId Long
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int save(ServeInfoDO model, Long groupId) {
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("serve.info.null"));
        Preconditions.checkArgument(
                StringUtils.isNotBlank(model.getName()),
                I18nMsgConfig.getMessage("serve.name.empty"));
        ServeGroupDO sgDo = Preconditions.checkNotNull(
                serveGroupMapper.findById(groupId),
                I18nMsgConfig.getMessage("serve.group.id.invalid"));
        Preconditions.checkArgument(
                StringUtils.equals(model.getServeType(), sgDo.getServeType()),
                I18nMsgConfig.getMessage("serve.type.notEqual"));

        // whether is updating.
        boolean isUpdate = false;
        if (model.getId() != null && model.getId() > 0) {
            boolean valid = checkServeId(model.getId());
            if (!valid) {
                throw new BizException(I18nMsgConfig.getMessage("serve.id.invalid"));
            }
            isUpdate = true;
        }

        RLock lock = redissonClient.getLock("SERVE_SAVE_" + model.getName() + (isUpdate ? model.getId() : "1"));
        if (lock.isLocked()) {
            throw new BizException(I18nMsgConfig.getMessage("global.get.lock.fail"));
        }
        try {
            lock.lock(2, TimeUnit.MINUTES);

            Preconditions.checkArgument(
                    serveInfoMapper.hasSameName(model.getName(), isUpdate ? model.getId() : null) <= 0,
                    I18nMsgConfig.getMessage("serve.name.exist"));
            // Checks if the configuration is correct
            if (StringUtils.isNotBlank(model.getConfigText())
                    && !StringUtils.equals(model.getConfigText(), "{}")) {
                ServeConfigTextDTO configTextDTO = JSON.parseObject(model.getConfigText(), ServeConfigTextDTO.class);
                checkServeConfigInfo(configTextDTO);
            } else {
                model.setConfigText(null);
            }

            int status;
            if (isUpdate) {
                status = serveInfoMapper.update(model);
                if (status > 0) {
                    serveRlGroupMapper.updateGroupIdByServeId(model.getId(), groupId);
                }
            } else {
                model.setOwnerId(SessionThreadLocal.getUserId());
                status = serveInfoMapper.insert(model);
                if (status > 0) {
                    serveRlGroupMapper.insert(new ServeRlGroupDO(model.getId(), groupId));
                }
            }

            return status;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Copy a new service with the specified service as the template.
     * If the version code is not empty, a version is used as a template.
     *
     * @param serveId Long
     * @param verCode String
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int copyById(Long serveId, String verCode) {
        Preconditions.checkNotNull(serveId, I18nMsgConfig.getMessage("serve.id.null"));
        ServeInfoDO siDo;
        if (StringUtils.isNotBlank(verCode)) {
            ServeVersionDO svDo = Preconditions.checkNotNull(
                    serveVersionMapper.findBy(serveId, verCode),
                    I18nMsgConfig.getMessage("serve.version.empty"));
            siDo = JSON.parseObject(svDo.getServeConfig(), ServeInfoDO.class);

        } else {
            siDo = Preconditions.checkNotNull(findById(serveId), I18nMsgConfig.getMessage("serve.id.invalid"));
        }

        siDo.setCode(CodeBuilderUtils.randomStr(CODE_LEN));
        String name = siDo.getName();
        int index;
        if ((index = StringUtils.indexOf(name, COPY_TAG)) > 0) {
            name = StringUtils.substring(name, 0, index) + CodeBuilderUtils.randomStr(COPY_TAG_RANDOM_LEN);
        } else {
            name = name + COPY_TAG + CodeBuilderUtils.randomStr(COPY_TAG_RANDOM_LEN);
        }
        siDo.setName(name);
        siDo.setOwnerId(SessionThreadLocal.getUserId());
        siDo.setId(null);
        return serveInfoMapper.insert(siDo);
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
        Preconditions.checkNotNull(id, I18nMsgConfig.getMessage("serve.id.null"));
        ServeInfoDO siDo = Preconditions.checkNotNull(findById(id), I18nMsgConfig.getMessage("serve.id.invalid"));
        ServeStatusType sst = ServeStatusType.getByCode(siDo.getStatus());
        if (sst != null && sst != ServeStatusType.ARCHIVE) {
            throw new BizException(I18nMsgConfig.getMessage("serve.del.restrict"));
        }

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
        Preconditions.checkArgument(id != null && id > 0, I18nMsgConfig.getMessage("serve.id.invalid"));
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
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("serve.info.null"));
        return serveInfoMapper.list(model);
    }

    /**
     * Whether the serveId Id is valid.
     *
     * @param serveId Long
     * @return boolean true - valid. false - invalid.
     */
    @Override
    public boolean checkServeId(Long serveId) {
        return serveInfoMapper.checkServeId(serveId) > 0;
    }

    /**
     * 1. change the serve status to PUBLISH;
     * 2. Save one version to the version list.
     *
     * @param serveId Long
     * @return int 1 - success, 0 - fail
     */
    @Override
    public int publishServe(Long serveId) {
        ServeInfoDO siDo = Preconditions.checkNotNull(
                serveInfoMapper.findById(serveId),
                I18nMsgConfig.getMessage("serve.id.invalid"));

        return 0;
    }

    /**
     * Rolls back the specified version of the service.
     *
     * @param serveId Long
     * @param verCode String
     * @return int 1 - success, 0 - fail
     */
    @Transactional
    @Override
    public int rollbackServe(Long serveId, String verCode) {
        ServeVersionDO svDo = Preconditions.checkNotNull(
                serveVersionMapper.findBy(serveId, verCode),
                I18nMsgConfig.getMessage("serve.version.empty"));
        ServeInfoDO siDo = JSON.parseObject(svDo.getServeConfig(), ServeInfoDO.class);
        siDo.setStatus(ServeStatusType.PUBLISH.name());
        int status = serveInfoMapper.update(siDo);
        if (status > 0) {
            serveVersionMapper.changePublish2History(serveId);

            svDo.setVersionType(VersionType.PUBLISH.name());
            serveVersionMapper.update(svDo);
        }
        return status;
    }

    /**
     * Move serve to another group.
     * <p>
     * One serve only link to one group.
     *
     * @param serveId Long
     * @param groupId Long
     * @return int 1 - success, 0 - fail
     */
    @Transactional
    @Override
    public int moveServe(Long serveId, Long groupId) {
        ServeInfoDO siDo = Preconditions.checkNotNull(
                serveInfoMapper.findById(serveId),
                I18nMsgConfig.getMessage("serve.id.invalid"));
        ServeGroupDO sgDo = Preconditions.checkNotNull(
                serveGroupMapper.findById(groupId),
                I18nMsgConfig.getMessage("serve.group.id.invalid"));
        Preconditions.checkArgument(
                StringUtils.equals(siDo.getServeType(), sgDo.getServeType()),
                I18nMsgConfig.getMessage("serve.type.notEqual"));

        return serveRlGroupMapper.updateGroupIdByServeId(serveId, groupId);
    }

    /**
     * Change the serve owner id.
     *
     * @param serveId Long
     * @param ownerId Long
     * @return int 1 - success, 0 - fail
     */
    @Override
    public int changeServeOwner(Long serveId, Long ownerId) {
        Preconditions.checkNotNull(
                serveInfoMapper.findById(serveId),
                I18nMsgConfig.getMessage("serve.id.invalid"));
        Preconditions.checkNotNull(
                accountService.findById(ownerId),
                I18nMsgConfig.getMessage("account.id.invalid"));
        return serveInfoMapper.changeServeOwner(serveId, ownerId);
    }

    /**
     * Checks if the configuration is correct.
     *
     * @param textDTO ServeConfigTextDTO
     */
    private void checkServeConfigInfo(ServeConfigTextDTO textDTO) {
        if (textDTO == null) {
            return;
        }

        // check the params.
        if (CollectionUtils.isNotEmpty(textDTO.getParams())) {
            checkServeParams(textDTO.getParams());
        }

        // check the columns.
        if (CollectionUtils.isNotEmpty(textDTO.getColumns())) {
            checkServeColumn(textDTO.getColumns());
        }

        // check the sql config.
        if (textDTO.getSql() != null) {
            checkServeSql(textDTO.getSql());
        }
    }

    /**
     * Judge the correctness of the serve query parameter information.
     *
     * @param params List
     */
    private void checkServeParams(List<ServeParamDTO> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }

        // Whether an argument with the same name exists.
        List<String> names = new ArrayList<>();
        for (ServeParamDTO p : params) {
            Preconditions.checkArgument(
                    StringUtils.isNotBlank(p.getName()),
                    I18nMsgConfig.getMessage("serve.param.name.empty"));
            Preconditions.checkArgument(
                    StringUtils.isNotBlank(p.getDataType()),
                    I18nMsgConfig.getMessage("serve.param.dataType.empty"));
            Preconditions.checkArgument(
                    p.getType() != null,
                    I18nMsgConfig.getMessage("serve.param.type.empty"));

            if (names.contains(p.getName())) {
                throw new BizException(I18nMsgConfig.getMessage("serve.param.name.multi", p.getName()));
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
            throw new BizException(I18nMsgConfig.getMessage("serve.param.interdependent", s));
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
                List<String> dependList = new ArrayList<>(Arrays.asList(arr));
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

    /**
     * Detection field information is valid information.
     *
     * @param columns List
     */
    private void checkServeColumn(List<ServeColumnDTO> columns) {

    }

    /**
     * Detection sql information is valid information
     *
     * @param sqlDTO ServeSqlDTO
     */
    private void checkServeSql(ServeSqlDTO sqlDTO) {

    }

}
