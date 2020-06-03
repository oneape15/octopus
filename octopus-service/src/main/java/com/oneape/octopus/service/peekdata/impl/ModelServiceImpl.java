package com.oneape.octopus.service.peekdata.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.datasource.QueryFactory;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.mapper.peekdata.ModelMapper;
import com.oneape.octopus.model.DO.peekdata.ModelDO;
import com.oneape.octopus.model.DO.peekdata.ModelMetaDO;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import com.oneape.octopus.model.VO.ModelMetaVO;
import com.oneape.octopus.model.VO.ModelVO;
import com.oneape.octopus.service.peekdata.ModelMetaService;
import com.oneape.octopus.service.peekdata.ModelService;
import com.oneape.octopus.service.schema.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ModelServiceImpl implements ModelService {
    @Resource
    private ModelMapper modelMapper;

    @Resource
    private ModelMetaService modelMetaService;
    @Resource
    private DatasourceService datasourceService;
    @Resource
    private QueryFactory queryFactory;

    /**
     * 根据对象进行查询
     *
     * @param model ModelDO
     * @return List
     */
    @Override
    public List<ModelVO> find(ModelDO model) {
        List<ModelDO> models = modelMapper.list(model);
        List<ModelVO> vos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(models)) {
            models.forEach(m -> vos.add(ModelVO.ofDO(m)));
        }
        return vos;
    }

    /**
     * 添加模型信息
     *
     * @param model ModelDO
     * @param metas List
     * @return int 0 - 失败； 1 - 成功；
     */
    @Override
    public int addModelInfo(ModelDO model, List<ModelMetaVO> metas) {
        int status = insert(model);

        if (status > 0) {
            modelMetaService.saveMetas(model.getId(), metas);
        }
        return status;
    }

    /**
     * 修改模型信息
     *
     * @param model ModelDO
     * @param metas List 有更改的字段信息
     * @return int 0 - 失败； 1 - 成功；
     */
    @Override
    public int editModelInfo(ModelDO model, List<ModelMetaVO> metas) {
        int status = edit(model);
        if (status > 0) {
            modelMetaService.saveMetas(model.getId(), metas);
        }
        return status;
    }

    /**
     * 获取指定模型的表字段信息
     *
     * @param modelId   Long
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    @Override
    public List<ModelMetaVO> getTableColumns(Long modelId, Long dsId, String tableName) {
        if (modelId == null || modelId <= 0) {
            DatasourceDO datasourceDO = Preconditions.checkNotNull(datasourceService.findById(dsId),
                    "数据源信息不存在");

            DatasourceInfo dsi = new DatasourceInfo();
            dsi.setUrl(datasourceDO.getJdbcUrl());
            dsi.setPassword(datasourceDO.getPassword());
            dsi.setUsername(datasourceDO.getUsername());
            dsi.setDatasourceType(DatasourceTypeHelper.byName(datasourceDO.getType()));

            String schema = queryFactory.getSchema(dsi);
            List<FieldInfo> fields = queryFactory.fieldOfTable(dsi, schema, tableName);

            final List<ModelMetaVO> metas = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(fields)) {
                fields.forEach(f -> {
                    ModelMetaVO vo = new ModelMetaVO();
                    vo.setId(-1L);
                    vo.setModelId(-1L);
                    vo.setTagId(-1L);
                    vo.setDisplay(1); // 显示

                    vo.setName(f.getName());
                    vo.setShowName(f.getName());

                    String dt = f.getDataType().name();
                    vo.setDataType(dt);
                    vo.setOriginDataType(dt);

                    vo.setComment(f.getComment());

                    metas.add(vo);
                });
            }

            return metas;
        } else {
            ModelMetaDO mmdo = new ModelMetaDO();
            mmdo.setModelId(modelId);
            return modelMetaService.find(mmdo);
        }
    }

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int insert(ModelDO model) {
        Preconditions.checkNotNull(model, "添加模型对象为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "模型名称为空");
        ModelDO tmp = new ModelDO();
        tmp.setName(model.getName());
        List<ModelDO> list = modelMapper.list(tmp);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 0) {
            throw new BizException(StateCode.BizError.getCode(), "存在相同名称的模型");
        }
        return modelMapper.insert(model);
    }

    /**
     * Modify the data.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int edit(ModelDO model) {
        Preconditions.checkNotNull(model, "添加模型对象为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "模型名称为空");
        ModelDO tmp = new ModelDO();
        tmp.setName(model.getName());
        List<ModelDO> list = modelMapper.list(tmp);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 0) {
            long size = list.stream().filter(m -> !model.getId().equals(m.getId())).count();
            if (size > 0) {
                throw new BizException(StateCode.BizError.getCode(), "存在相同名称的模型");
            }
        }
        return modelMapper.update(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    @Transactional
    public int deleteById(ModelDO model) {
        Preconditions.checkNotNull(model, "添加模型对象为空");
        int status = modelMapper.delete(model);
        if (status > 0) {
            modelMetaService.delByModelId(model.getId());
        }
        return status;
    }

    /**
     * 根据Id获取模型信息
     *
     * @param modelId Long
     * @return ModelVO
     */
    @Override
    @Transactional
    public ModelVO getById(Long modelId) {
        Preconditions.checkNotNull(modelId, "模型Id为空");
        ModelDO mdo = modelMapper.findById(modelId);
        if (mdo == null) {
            throw new BizException("模型信息不存在");
        }
        ModelVO vo = ModelVO.ofDO(mdo);
        vo.setFields(listModelMeta(new ModelMetaDO(modelId)));

        return vo;
    }

    /**
     * 获取指定模型的表字段信息
     *
     * @param mm ModelMetaDO
     * @return List
     */
    @Override
    public List<ModelMetaVO> listModelMeta(ModelMetaDO mm) {
        return modelMetaService.find(mm);
    }

    /**
     * 修改模型状态
     *
     * @param modelId Long
     * @param status  Integer
     * @return int
     */
    @Override
    public int changeStatus(Long modelId, Integer status) {
        Preconditions.checkNotNull(modelId, "主键为空");
        Preconditions.checkArgument(status == 1 || status == 0, "状态值无效");

        ModelDO model = new ModelDO();
        model.setId(modelId);
        model.setStatus(status);
        return modelMapper.update(model);
    }
}
