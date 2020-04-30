package com.oneape.octopus.service.peekdata.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.mapper.peekdata.ModelTagMapper;
import com.oneape.octopus.model.DO.peekdata.ModelTagDO;
import com.oneape.octopus.model.VO.ModelTagVO;
import com.oneape.octopus.service.peekdata.ModelTagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ModelTagServiceImpl implements ModelTagService {
    @Resource
    private ModelTagMapper modelTagMapper;

    /**
     * 根据对象进行查询
     *
     * @param modelTag ModelTagDO
     * @return List
     */
    @Override
    public List<ModelTagVO> find(ModelTagDO modelTag) {
        List<ModelTagDO> list = modelTagMapper.listOrLink(modelTag);

        List<ModelTagVO> vos = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return vos;
        }

        list.forEach(mt -> vos.add(ModelTagVO.ofDO(mt)));

        return vos;
    }

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(ModelTagDO model) {
        Preconditions.checkNotNull(model, "标签对象为空");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getRule()), "标签名或规则为空");

        ModelTagDO tmp = new ModelTagDO();
        tmp.setName(model.getName());
        tmp.setRule(model.getRule());
        tmp.setArchive(null);
        List<ModelTagDO> list = modelTagMapper.listOrLink(tmp);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 0) {
            throw new BizException(String.format("已经存在标签名称为:%s或匹配值为:%s的记录", model.getName(), model.getRule()));
        }
        return modelTagMapper.insert(model);
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(ModelTagDO model) {
        Preconditions.checkNotNull(model, "标签对象为空");
        Preconditions.checkArgument(model.getId() != null && model.getId() > 0, "主键为空");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getRule()), "标签名或规则为空");

        ModelTagDO tmp = new ModelTagDO();
        tmp.setName(model.getName());
        tmp.setRule(model.getRule());
        List<ModelTagDO> list = modelTagMapper.listOrLink(tmp);
        if (CollectionUtils.isNotEmpty(list)) {
            long size = list.stream().filter(mt -> !model.getId().equals(mt.getId())).count();
            if (size > 0) {
                throw new BizException(String.format("已经存在标签名称为:%s或匹配值为:%s的记录", model.getName(), model.getRule()));
            }
        }
        return modelTagMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(ModelTagDO model) {
        Preconditions.checkNotNull(model, "标签对象为空");
        Preconditions.checkArgument(model.getId() != null && model.getId() > 0, "主键为空");
        return modelTagMapper.delete(model);
    }
}
