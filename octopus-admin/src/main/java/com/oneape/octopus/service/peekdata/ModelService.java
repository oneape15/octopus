package com.oneape.octopus.service.peekdata;

import com.oneape.octopus.model.domain.peekdata.ModelDO;
import com.oneape.octopus.model.domain.peekdata.ModelMetaDO;
import com.oneape.octopus.model.VO.ModelMetaVO;
import com.oneape.octopus.model.VO.ModelVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface ModelService extends BaseService<ModelDO> {

    /**
     * 添加模型信息
     *
     * @param model ModelDO
     * @param metas List
     * @return int 0 - 失败； 1 - 成功；
     */
    int addModelInfo(ModelDO model, List<ModelMetaVO> metas);

    /**
     * 修改模型信息
     *
     * @param model ModelDO
     * @param metas List 有更改的字段信息
     * @return int 0 - 失败； 1 - 成功；
     */
    int editModelInfo(ModelDO model, List<ModelMetaVO> metas);

    /**
     * 获取指定模型的表字段信息
     *
     * @param modelId   Long
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    List<ModelMetaDO> getTableColumns(Long modelId, Long dsId, String tableName);

    /**
     * 根据Id获取模型信息
     *
     * @param modelId Long
     * @return ModelVO
     */
    ModelVO getById(Long modelId);

    /**
     * 获取指定模型的表字段信息
     *
     * @param mm ModelMetaDO
     * @return List
     */
    List<ModelMetaDO> listModelMeta(ModelMetaDO mm);

    /**
     * 修改模型状态
     *
     * @param modelId Long
     * @param status  Integer
     * @return int
     */
    int changeStatus(Long modelId, Integer status);
}
