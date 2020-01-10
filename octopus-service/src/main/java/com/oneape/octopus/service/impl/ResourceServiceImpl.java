package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.system.ResourceMapper;
import com.oneape.octopus.mapper.system.RoleRlResourceMapper;
import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.DO.system.RoleRlResourceDO;
import com.oneape.octopus.model.VO.MenuVO;
import com.oneape.octopus.model.VO.ResourceVO;
import com.oneape.octopus.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(ResourceDO model) {
        Assert.isTrue(StringUtils.isNotBlank(model.getName()), "资源名称为空");

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
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    @Transactional
    public int edit(ResourceDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");
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
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(ResourceDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");

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
        // 设置排序方式
        List<String> orders = new ArrayList<>();
        orders.add("level");
        orders.add("sort_id DESC");
        List<ResourceDO> resources = resourceMapper.listWithOrder(resource, orders);

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
}
