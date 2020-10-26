package com.oneape.octopus.service.peekdata.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.mapper.peekdata.ModelTagMapper;
import com.oneape.octopus.model.domain.peekdata.ModelTagDO;
import com.oneape.octopus.service.peekdata.ModelTagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ModelTagServiceImpl implements ModelTagService {
    @Resource
    private ModelTagMapper modelTagMapper;

    /**
     * Query against an object.
     *
     * @param modelTag ModelTagDO
     * @return List
     */
    @Override
    public List<ModelTagDO> find(ModelTagDO modelTag) {
        return modelTagMapper.listOrLink(modelTag);
    }

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(ModelTagDO model) {
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

        if (model.getId() != null) {
            return modelTagMapper.update(model);
        }
        return modelTagMapper.insert(model);
    }

    /**
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(ModelTagDO model) {
        return modelTagMapper.update(model);
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public ModelTagDO findById(Long id) {
        return modelTagMapper.findById(id);
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        Preconditions.checkArgument(id != null && id > 0, "主键为空");
        return modelTagMapper.delete(new ModelTagDO(id));
    }
}
