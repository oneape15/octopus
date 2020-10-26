package com.oneape.octopus.service.peekdata;

import com.oneape.octopus.model.domain.peekdata.ModelMetaDO;
import com.oneape.octopus.model.VO.ModelMetaVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface ModelMetaService extends BaseService<ModelMetaDO> {

    /**
     * 保存模型元素信息
     *
     * @param modelId Long
     * @param metas   List
     * @return int 0 - fail; 1 - success.
     */
    int saveMetas(Long modelId, List<ModelMetaVO> metas);

    /**
     * 根据模型Id删除
     *
     * @param modelId Long
     * @return int 0 - fail; 1 - success.
     */
    int delByModelId(Long modelId);
}
