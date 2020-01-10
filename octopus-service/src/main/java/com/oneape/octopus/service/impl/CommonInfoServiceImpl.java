package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.mapper.system.CommonInfoMapper;
import com.oneape.octopus.model.DO.system.CommonInfoDO;
import com.oneape.octopus.model.VO.CommonInfoVO;
import com.oneape.octopus.service.CommonInfoService;
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
        Assert.isTrue(commonInfo != null, "对象为空");
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
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(CommonInfoDO model) {
        Assert.isTrue(model != null, "基础信息对象为空");
        Assert.isTrue(StringUtils.isNoneBlank(model.getClassify(), model.getName(), model.getCode()), "信息分类、名称或编码为空");

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
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(CommonInfoDO model) {
        Assert.isTrue(model != null, "基础信息对象为空");
        Assert.isTrue(StringUtils.isNoneBlank(model.getClassify(), model.getName(), model.getCode()), "信息分类、名称或编码为空");
        Assert.isTrue(model.getId() != null, "主键为空");

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
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(CommonInfoDO model) {
        Assert.isTrue(model != null, "基础信息对象为空");
        Assert.isTrue(model.getId() != null, "主键为空");
        CommonInfoDO tmp = new CommonInfoDO();
        tmp.setParentId(model.getId());
        int childrenSize = commonInfoMapper.size(tmp);
        if (childrenSize > 0) {
            throw new BizException("还存在子节点，不允许删除");
        }
        return commonInfoMapper.delete(model);
    }
}
