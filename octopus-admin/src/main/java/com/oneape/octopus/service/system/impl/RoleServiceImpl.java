package com.oneape.octopus.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.common.MaskUtils;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.system.RoleMapper;
import com.oneape.octopus.mapper.system.RoleRlResourceMapper;
import com.oneape.octopus.mapper.system.RoleRlSchemaMapper;
import com.oneape.octopus.mapper.system.UserRlRoleMapper;
import com.oneape.octopus.model.domain.system.RoleDO;
import com.oneape.octopus.model.domain.system.RoleRlResourceDO;
import com.oneape.octopus.model.domain.system.RoleRlSchemaDO;
import com.oneape.octopus.service.schema.DatasourceService;
import com.oneape.octopus.service.system.RoleService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper           roleMapper;
    @Resource
    private UserRlRoleMapper     userRlRoleMapper;
    @Resource
    private RoleRlResourceMapper roleRlResourceMapper;
    @Resource
    private RoleRlSchemaMapper   roleRlSchemaMapper;

    @Resource
    private DatasourceService   datasourceService;
    @Resource
    private SqlSessionFactory   sqlSessionFactory;
    @Resource
    private UIDGeneratorService uidGeneratorService;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(RoleDO model) {
        Preconditions.checkNotNull(model, "The role information is null.");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getCode()), "The role name or code is empty.");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getCode()), "The role name or code is empty.");

        // Determine if the code or name is repeated.
        int count = roleMapper.getSameNameOrCodeRole(model.getName(), model.getCode(), model.getId());
        if (count > 0) {
            throw new BizException("The name or code for the role already exists.");
        }

        if (model.getId() != null) {
            return roleMapper.update(model);
        }
        return roleMapper.insert(model);
    }

    /**
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(RoleDO model) {
        return 0;
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public RoleDO findById(Long id) {
        return null;
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        Preconditions.checkNotNull(id, "The role primary key is empty.");

        int size = userRlRoleMapper.getUseSize(id);
        if (size > 0) {
            throw new BizException("The current role is still in use and cannot be deleted.");
        }
        return roleMapper.delete(new RoleDO(id));
    }

    /**
     * Get the user role list.
     *
     * @param userId Long
     * @return List
     */
    @Override
    public List<RoleDO> findRoleByUserId(Long userId) {
        if (userId == null || userId < 1L) {
            return new ArrayList<>();
        }
        return roleMapper.findRoleByUserId(userId);
    }

    /**
     * Query resources by condition.
     *
     * @param role RoleDO
     * @return List
     */
    @Override
    public List<RoleDO> find(RoleDO role) {
        List<String> orders = new ArrayList<>();
        orders.add(BaseSqlProvider.FIELD_CREATED + " DESC");
        List<RoleDO> list = roleMapper.listWithOrder(role, orders);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        return list;
    }


    /**
     * Gets resource permissions based on the list of role ids.
     *
     * @param roleIds List
     * @return Map
     */
    @Override
    public Map<Long, Set<Integer>> getRoleRes(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new HashMap<>();
        }
        List<RoleRlResourceDO> list = roleRlResourceMapper.getResIdByRoleIds(roleIds);
        Map<Long, Set<Integer>> retMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (RoleRlResourceDO rdo : list) {
                Long resId = rdo.getResourceId();
                if (!retMap.containsKey(resId)) {
                    retMap.put(resId, new HashSet<>());
                }
                List<Integer> maskList = MaskUtils.getList(rdo.getMask());
                retMap.get(resId).addAll(maskList);
            }
        }
        return retMap;
    }

    /**
     * Delete the relationship between the user and the role.
     *
     * @param userId Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteRelationshipWithUserId(Long userId) {
        return userRlRoleMapper.deleteByUserId(userId);
    }

    /**
     * Save the association between the role and the table.
     *
     * @param rrsdo RoleRlSchemaDO
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int saveRoleRlSchema(RoleRlSchemaDO rrsdo) {
        Preconditions.checkNotNull(rrsdo, "The object is null.");
        Preconditions.checkArgument(StringUtils.isNotBlank(rrsdo.getTableName()), "The schema table is empty.");
        Preconditions.checkArgument(rrsdo.getExpireTime() != null && rrsdo.getExpireTime() > System.currentTimeMillis(),
                "Invalid expiration time.");
        Preconditions.checkNotNull(roleMapper.findById(rrsdo.getId()), "The role is not exist.");
        Preconditions.checkArgument(datasourceService.isExistDsId(rrsdo.getDatasourceId()), "The datasource is not exist.");

        int status;
        if (rrsdo.getId() != null) {
            status = roleRlSchemaMapper.update(rrsdo);
        } else {
            status = roleRlSchemaMapper.insert(rrsdo);
        }
        return status;
    }

    /**
     * Save role and data table information in bulk.
     *
     * @param roleId Long
     * @param list   List<RoleRlSchemaDO>
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int batchSaveRoleRlSchema(Long roleId, List<RoleRlSchemaDO> list) {
        Preconditions.checkNotNull(roleMapper.findById(roleId), "The role is not exist.");
        if (CollectionUtils.isEmpty(list)) {
            return roleRlSchemaMapper.deleteByRoleId(roleId);
        }

        List<RoleRlSchemaDO> oldRelationship = roleRlSchemaMapper.findByRoleId(roleId);
        Map<String, Long> oldMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(oldRelationship)) {
            oldRelationship.forEach(l -> oldMap.put(l.getDatasourceId() + "_" + l.getTableName(), l.getId()));
        }

        List<RoleRlSchemaDO> updateList = new ArrayList<>();
        List<RoleRlSchemaDO> insertList = new ArrayList<>();
        List<String> existList = new ArrayList<>();

        for (RoleRlSchemaDO rrsdo : list) {
            Preconditions.checkArgument(rrsdo.getDatasourceId() != null && rrsdo.getDatasourceId() > 0, "The datasource is invalid.");
            Preconditions.checkArgument(StringUtils.isNotBlank(rrsdo.getTableName()), "The schema table is empty.");
            Preconditions.checkArgument(rrsdo.getExpireTime() != null && rrsdo.getExpireTime() > System.currentTimeMillis(),
                    "Invalid expiration time.");

            rrsdo.setRoleId(roleId);
            String key = rrsdo.getDatasourceId() + "_" + rrsdo.getTableName();
            if (oldMap.containsKey(key)) {
                rrsdo.setId(oldMap.get(key));
                updateList.add(rrsdo);
                existList.add(key);
            } else {
                rrsdo.setId(uidGeneratorService.getUid());
                insertList.add(rrsdo);
            }
        }

        // get the need delete ids;
        Set<String> needDeleteKeySet = oldMap.keySet();
        needDeleteKeySet.removeAll(existList);
        List<Long> needDeleteIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(needDeleteKeySet)) {
            needDeleteKeySet.forEach(k -> needDeleteIds.add(oldMap.get(k)));
        }


        // bath options
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            RoleRlSchemaMapper mapper = session.getMapper(RoleRlSchemaMapper.class);
            updateList.forEach(l -> mapper.update(l));
            insertList.forEach(l -> mapper.insert(l));

            // delete the not exist table schema.
            if (CollectionUtils.isNotEmpty(needDeleteIds)) {
                mapper.deleteByIds(needDeleteIds);
            }

            session.commit();
        } catch (Exception e) {
            log.error("Save role and data table information in bulk exception.", e);
            session.rollback();
            throw new BizException("Save role and data table information in bulk exception.", e);
        } finally {
            session.close();
        }
        return 1;
    }
}
