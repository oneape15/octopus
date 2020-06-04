package com.oneape.octopus.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.common.MaskUtils;
import com.oneape.octopus.model.enums.FixOptionType;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.mapper.system.ResourceMapper;
import com.oneape.octopus.mapper.system.RoleRlResourceMapper;
import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.DO.system.RoleRlResourceDO;
import com.oneape.octopus.model.VO.ResourceVO;
import com.oneape.octopus.model.VO.TreeNodeVO;
import com.oneape.octopus.service.system.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private RoleRlResourceMapper roleRlResourceMapper;

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int insert(ResourceDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "资源名称为空");

        Long parentId = model.getParentId();
        if (parentId == null) {
            parentId = GlobalConstant.DEFAULT_VALUE;
        }
        List<ResourceDO> resources = resourceMapper.list(new ResourceDO(parentId, model.getName()));
        if (CollectionUtils.isNotEmpty(resources)) {
            throw new BizException("存在相同名称的资源~");
        }

        // 设置level值
        if (parentId <= GlobalConstant.DEFAULT_VALUE) {
            model.setLevel(1);
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        } else {
            ResourceDO resource = resourceMapper.findById(parentId);
            if (resource == null) {
                throw new BizException("上级资源不存在~");
            }
            model.setLevel(resource.getLevel() + 1);
        }

        return resourceMapper.insert(model);
    }

    /**
     * Modify the data.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    @Transactional
    public int edit(ResourceDO model) {
        Preconditions.checkNotNull(model.getId(), "The primary Key is empty.");
        ResourceDO old = resourceMapper.findById(model.getId());
        if (old == null) {
            throw new BizException("需要修改的资源不存在");
        }

        Long parentId = model.getParentId();
        if (parentId == null) {
            parentId = GlobalConstant.DEFAULT_VALUE;
        }
        if (parentId <= GlobalConstant.DEFAULT_VALUE) {
            model.setLevel(1);
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        } else {
            ResourceDO resource = resourceMapper.findById(model.getParentId());
            if (resource == null) {
                throw new BizException("上级资源不存在~");
            }
            model.setLevel(resource.getLevel() + 1);

            List<ResourceDO> resources = resourceMapper.list(new ResourceDO(parentId, model.getName()));
            if (CollectionUtils.isNotEmpty(resources)) {
                long size = resources.stream().filter(tmp -> !model.getId().equals(tmp.getId())).count();
                if (size > 0) {
                    throw new BizException("存在相同名称的资源~");
                }
            }
        }

        int status = resourceMapper.update(model);

        // 如果父节点变更更了，则需要把其子节点的level同步修改掉
        if (status > 0 && !parentId.equals(old.getParentId())) {
            editChildrenLevel(model.getId(), model.getLevel());
        }
        return status;
    }

    /**
     * 修改对应节点下的子节点level值
     *
     * @param parentId    Long
     * @param parentLevel Integer
     */
    private void editChildrenLevel(Long parentId, Integer parentLevel) {
        List<ResourceDO> children = resourceMapper.list(new ResourceDO(parentId, null));
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        for (ResourceDO rdo : children) {
            ResourceDO tmp = new ResourceDO();
            tmp.setId(rdo.getId());
            tmp.setLevel(parentLevel + 1);
            int status = resourceMapper.update(tmp);
            if (status > 0) {
                editChildrenLevel(tmp.getId(), tmp.getLevel());
            }
        }
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(ResourceDO model) {
        Preconditions.checkNotNull(model.getId(), "The primary Key is empty.");

        int status = resourceMapper.delete(model);
        if (status > GlobalConstant.FAIL) {
            roleRlResourceMapper.delete(new RoleRlResourceDO(null, model.getId()));
        }
        return status;
    }

    /**
     * 根据条件查询资源
     *
     * @param resource ResourceDO
     * @return List
     */
    @Override
    public List<ResourceVO> findTree(ResourceDO resource) {
        Pair<List<Integer>, Map<Integer, List<ResourceDO>>> pair = getLevelInfo(resource);
        List<Integer> levels = pair.getLeft();
        Map<Integer, List<ResourceDO>> levelMap = pair.getRight();

        // 从下往上遍历
        Map<Long, List<ResourceVO>> preLevelMap = new LinkedHashMap<>();
        for (Integer level : levels) {
            Map<Long, List<ResourceVO>> curLevelMap = new LinkedHashMap<>();
            for (ResourceDO r : levelMap.get(level)) {
                Long id = r.getId();
                Long pId = r.getParentId();
                ResourceVO vo = ResourceVO.ofDO(r);
                if (preLevelMap.containsKey(id)) {
                    vo.setChildren(preLevelMap.get(id));
                }
                if (!curLevelMap.containsKey(pId)) {
                    curLevelMap.put(pId, new ArrayList<>());
                }
                curLevelMap.get(pId).add(vo);
            }
            preLevelMap = curLevelMap;
        }

        List<ResourceVO> list = new ArrayList<>();
        preLevelMap.values().forEach(list::addAll);

        return list;
    }

    /**
     * 获取整棵资源树
     *
     * @return List
     */
    @Override
    public List<TreeNodeVO> fullTree() {
        Pair<List<Integer>, Map<Integer, List<ResourceDO>>> pair = getLevelInfo(new ResourceDO());
        List<Integer> levels = pair.getLeft();
        Map<Integer, List<ResourceDO>> levelMap = pair.getRight();

        // 从下往上遍历
        Map<Long, List<TreeNodeVO>> preLevelMap = new LinkedHashMap<>();
        for (Integer level : levels) {
            Map<Long, List<TreeNodeVO>> curLevelMap = new LinkedHashMap<>();
            for (ResourceDO r : levelMap.get(level)) {
                Long id = r.getId();
                Long pId = r.getParentId();
                TreeNodeVO vo = new TreeNodeVO(id + "", r.getName(), r.getIcon());

                if (preLevelMap.containsKey(id)) {
                    vo.setChildren(preLevelMap.get(id));
                    vo.setLeaf(false);
                } else {
                    vo.setLeaf(true);
                }
                if (!curLevelMap.containsKey(pId)) {
                    curLevelMap.put(pId, new ArrayList<>());
                }
                curLevelMap.get(pId).add(vo);
            }
            preLevelMap = curLevelMap;
        }

        List<TreeNodeVO> list = new ArrayList<>();
        preLevelMap.values().forEach(list::addAll);
        TreeNodeVO rootNode = FixOptionType.NULL.getNode();
        rootNode.setChildren(list);
        return Collections.singletonList(rootNode);
    }

    /**
     * 获取层次结构信息
     *
     * @param model ResourceDO
     * @return Pair
     */
    private Pair<List<Integer>, Map<Integer, List<ResourceDO>>> getLevelInfo(ResourceDO model) {
        // 设置排序方式
        List<String> orders = new ArrayList<>();
        orders.add("level");
        orders.add("sort_id DESC");
        List<ResourceDO> resources = resourceMapper.listWithOrder(model, orders);

        Map<Integer, List<ResourceDO>> levelMap = new HashMap<>();
        for (ResourceDO r : resources) {
            if (!levelMap.containsKey(r.getLevel())) {
                levelMap.put(r.getLevel(), new ArrayList<>());
            }
            levelMap.get(r.getLevel()).add(r);
        }

        List<Integer> levels = levelMap.keySet()
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return new Pair<>(levels, levelMap);
    }

    /**
     * 获取指定角色拥有的资源权限集合
     *
     * @param roleId Long 角色Id
     * @return Map
     */
    @Override
    public Map<Long, List<Integer>> getByRoleId(Long roleId) {
        Map<Long, List<Integer>> ret = new HashMap<>();
        List<RoleRlResourceDO> list = roleRlResourceMapper.getResIdByRoleIds(Collections.singletonList(roleId));
        if (CollectionUtils.isEmpty(list)) {
            return ret;
        }

        list.forEach(r -> ret.put(r.getResourceId(), MaskUtils.getList(r.getMask())));
        return ret;
    }
}
