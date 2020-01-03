package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.mapper.ReportGroupMapper;
import com.oneape.octopus.model.DO.ReportGroupDO;
import com.oneape.octopus.model.VO.ReportGroupVO;
import com.oneape.octopus.service.ReportGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReportGroupServiceImpl implements ReportGroupService {

    @Resource
    private ReportGroupMapper reportGroupMapper;

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(ReportGroupDO model) {
        Assert.isTrue(StringUtils.isNotBlank(model.getName()), "报表组名称为空");

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
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(ReportGroupDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");

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
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(ReportGroupDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");
        return reportGroupMapper.delete(model);
    }

    /**
     * 根据报表组信息查询
     *
     * @param group ReportGroupDO
     * @return List
     */
    @Override
    public List<ReportGroupVO> listBy(ReportGroupDO group) {
        List<ReportGroupDO> groups = reportGroupMapper.list(group);
        List<ReportGroupVO> vos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groups)) {
            groups.forEach(g -> vos.add(ReportGroupVO.ofDO(g)));
        }
        return vos;
    }
}
