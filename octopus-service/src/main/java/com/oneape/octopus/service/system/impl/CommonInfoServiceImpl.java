package com.oneape.octopus.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.mapper.system.CommonInfoMapper;
import com.oneape.octopus.model.DO.system.CommonInfoDO;
import com.oneape.octopus.model.VO.CommonInfoVO;
import com.oneape.octopus.service.system.CommonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommonInfoServiceImpl implements CommonInfoService {

    @Resource
    private CommonInfoMapper commonInfoMapper;

    /**
     * 根据对象进行查询
     *
     * @param commonInfo CommonInfoDO
     * @return List
     */
    @Override
    public List<CommonInfoVO> find(CommonInfoDO commonInfo) {
        Preconditions.checkNotNull(commonInfo, "对象为空");
        List<CommonInfoDO> cidos = commonInfoMapper.list(commonInfo);

        List<CommonInfoVO> dvo = new ArrayList<>();
        if (CollectionUtils.isEmpty(cidos)) {
            return dvo;
        }
        cidos.forEach(ddo -> dvo.add(CommonInfoVO.ofDO(ddo)));
        return dvo;
    }

    /**
     * 获取所有分类信息
     *
     * @return List
     */
    @Override
    public List<String> getAllClassify() {
        return commonInfoMapper.getAllClassify();
    }

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int insert(CommonInfoDO model) {
        Preconditions.checkNotNull(model, "基础信息对象为空");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getClassify(), model.getName(), model.getCode()), "信息分类、名称或编码为空");

        if (model.getParentId() == null || model.getParentId() < GlobalConstant.DEFAULT_VALUE) {
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        }

        CommonInfoDO tmp = new CommonInfoDO();
        tmp.setParentId(model.getParentId());
        tmp.setClassify(model.getClassify());
        tmp.setCode(model.getCode());
        List<CommonInfoDO> list = commonInfoMapper.list(tmp);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizException("存在相同的编码");
        }

        return commonInfoMapper.insert(model);
    }

    /**
     * Modify the data.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(CommonInfoDO model) {
        Preconditions.checkNotNull(model, "基础信息对象为空");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getClassify(), model.getName(), model.getCode()), "信息分类、名称或编码为空");
        Preconditions.checkNotNull(model.getId(), "主键为空");

        if (model.getParentId() == null || model.getParentId() < GlobalConstant.DEFAULT_VALUE) {
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        }

        CommonInfoDO tmp = new CommonInfoDO();
        tmp.setParentId(model.getParentId());
        tmp.setClassify(model.getClassify());
        tmp.setCode(model.getCode());
        List<CommonInfoDO> list = commonInfoMapper.list(tmp);
        if (CollectionUtils.isNotEmpty(list)) {
            long size = list.stream().filter(r -> !r.getId().equals(model.getId())).count();
            if (size > 0) {
                throw new BizException("存在相同的编码");
            }
        }

        return commonInfoMapper.update(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(CommonInfoDO model) {
        Preconditions.checkNotNull(model, "基础信息对象为空");
        Preconditions.checkNotNull(model.getId(), "主键为空");
        CommonInfoDO tmp = new CommonInfoDO();
        tmp.setParentId(model.getId());
        int childrenSize = commonInfoMapper.size(tmp);
        if (childrenSize > 0) {
            throw new BizException("还存在子节点，不允许删除");
        }
        return commonInfoMapper.delete(model);
    }
}
