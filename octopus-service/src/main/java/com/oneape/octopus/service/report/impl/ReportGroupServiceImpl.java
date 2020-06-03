package com.oneape.octopus.service.report.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.mapper.report.ReportGroupMapper;
import com.oneape.octopus.model.DO.report.ReportGroupDO;
import com.oneape.octopus.model.VO.ReportGroupVO;
import com.oneape.octopus.model.VO.TreeNodeVO;
import com.oneape.octopus.service.report.ReportGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportGroupServiceImpl implements ReportGroupService {

    @Resource
    private ReportGroupMapper reportGroupMapper;

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int insert(ReportGroupDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "报表组名称为空");

        Long parentId = model.getParentId();
        if (parentId == null) {
            parentId = GlobalConstant.DEFAULT_VALUE;
        }
        List<ReportGroupDO> groups = reportGroupMapper.list(new ReportGroupDO(parentId, model.getName()));
        if (CollectionUtils.isNotEmpty(groups)) {
            throw new BizException("存在相同名称的报表组~");
        }

        // 设置level值
        if (parentId <= GlobalConstant.DEFAULT_VALUE) {
            model.setLevel(1);
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        } else {
            ReportGroupDO group = reportGroupMapper.findById(parentId);
            if (group == null) {
                throw new BizException("上级报表组不存在~");
            }
            model.setLevel(group.getLevel() + 1);
        }

        return reportGroupMapper.insert(model);
    }

    /**
     * Modify the data.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int edit(ReportGroupDO model) {
        Preconditions.checkNotNull(model.getId(), "The primary Key is empty.");

        Long parentId = model.getParentId();
        if (parentId == null) {
            parentId = GlobalConstant.DEFAULT_VALUE;
        }
        if (parentId <= GlobalConstant.DEFAULT_VALUE) {
            model.setLevel(1);
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        } else {
            ReportGroupDO group = reportGroupMapper.findById(model.getParentId());
            if (group == null) {
                throw new BizException("上级报表组不存在~");
            }
            model.setLevel(group.getLevel() + 1);

            List<ReportGroupDO> groups = reportGroupMapper.list(new ReportGroupDO(parentId, model.getName()));
            if (CollectionUtils.isNotEmpty(groups)) {
                long size = groups.stream().filter(tmp -> !model.getId().equals(tmp.getId())).count();
                if (size > 0) {
                    throw new BizException("存在相同名称的报表组~");
                }
            }
        }

        return reportGroupMapper.update(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int deleteById(ReportGroupDO model) {
        Preconditions.checkNotNull(model.getId() , "The primary Key is empty.");
        ReportGroupDO query = new ReportGroupDO();
        query.setParentId(model.getId());
        int size = reportGroupMapper.size(query);
        if (size > 0) {
            throw new BizException(StateCode.BizError.getCode(), "报表组还存在子节点，不能被删除");
        }
        return reportGroupMapper.delete(model);
    }

    /**
     * 根据报表组信息查询
     *
     * @param group ReportGroupDO
     * @return List
     */
    @Override
    public List<ReportGroupVO> find(ReportGroupDO group) {
        return reportGroupMapper.listWithChildrenSize(group);
    }

    /**
     * 获取报表组树型结构
     *
     * @return List
     */
    @Override
    public List<TreeNodeVO> getGroupTree() {
        // 设置排序方式
        List<String> orders = new ArrayList<>();
        orders.add("level");
        orders.add("sort_id DESC");
        List<ReportGroupDO> groups = reportGroupMapper.listWithOrder(new ReportGroupDO(), orders);

        Map<Integer, List<ReportGroupDO>> levelMap = new HashMap<>();
        for (ReportGroupDO rg : groups) {
            if (!levelMap.containsKey(rg.getLevel())) {
                levelMap.put(rg.getLevel(), new ArrayList<>());
            }
            levelMap.get(rg.getLevel()).add(rg);
        }

        List<Integer> levels = levelMap.keySet()
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // 从下往上遍历
        Map<Long, List<TreeNodeVO>> preLevelMap = new LinkedHashMap<>();
        for (Integer level : levels) {
            Map<Long, List<TreeNodeVO>> curLevelMap = new LinkedHashMap<>();
            for (ReportGroupDO r : levelMap.get(level)) {
                Long id = r.getId();
                Long pId = r.getParentId();
                TreeNodeVO vo = new TreeNodeVO();
                vo.setKey(r.getId() + "");
                vo.setValue(vo.getKey());
                vo.setTitle(r.getName());
                vo.setIcon(r.getIcon());
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

        List<TreeNodeVO> list = new ArrayList<>();
        preLevelMap.values().forEach(list::addAll);

        TreeNodeVO root = new TreeNodeVO("0", "根节点", "bars");
        root.setChildren(list);

        List<TreeNodeVO> ret = new ArrayList<>();
        ret.add(root);

        return ret;
    }
}
