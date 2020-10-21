package com.oneape.octopus.service.peekdata.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.mapper.peekdata.ModelMetaMapper;
import com.oneape.octopus.model.DO.peekdata.ModelMetaDO;
import com.oneape.octopus.model.VO.ModelMetaVO;
import com.oneape.octopus.service.peekdata.ModelMetaService;
import com.oneape.octopus.service.system.AccountService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ModelMetaServiceImpl implements ModelMetaService {
    @Resource
    private ModelMetaMapper modelMetaMapper;

    @Resource
    private SqlSessionFactory   sqlSessionFactory;
    @Resource
    private UIDGeneratorService uidGeneratorService;
    @Resource
    private AccountService      accountService;


    /**
     * 保存模型元素信息
     *
     * @param modelId Long
     * @param metas   List
     * @return int 0 - fail; 1 - success.
     */
    @Override
    public int saveMetas(Long modelId, List<ModelMetaVO> metas) {
        if (modelId == null || modelId < 0) {
            throw new BizException("模型Id为空");
        }
        if (CollectionUtils.isEmpty(metas)) return 1;

        List<Long> ids = new ArrayList<>();
        metas.forEach(m -> {
            if (m.getId() != null) {
                ids.add(m.getId());
            }
        });

        // 删除已经存在的元素信息
        modelMetaMapper.delByIds(ids);

        // 批量添加新的查询参数
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            ModelMetaMapper mapper = session.getMapper(ModelMetaMapper.class);
            for (ModelMetaVO mmo : metas) {
                ModelMetaDO mmdo = mmo.toDO();
                mmdo.setModelId(modelId);
                mmdo.setId(uidGeneratorService.getUid());
                mmdo.setCreator(accountService.getCurrentUserId());
                mapper.insert(mmdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("批量插入模型元素参数异常", e);
            session.rollback();
        } finally {
            session.close();
        }

        return count == metas.size() ? 1 : 0;
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
    public int save(ModelMetaDO model) {
        Preconditions.checkNotNull(model, "新增数据对象为空");

        if (model.getId() != null) {
            return modelMetaMapper.update(model);
        }
        return modelMetaMapper.insert(model);
    }

    /**
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(ModelMetaDO model) {
        return 0;
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        ModelMetaDO mdo = new ModelMetaDO();
        mdo.setId(id);
        return modelMetaMapper.delete(mdo);
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public ModelMetaDO findById(Long id) {
        return modelMetaMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<ModelMetaDO> find(ModelMetaDO model) {
        return modelMetaMapper.list(model);
    }

    /**
     * 根据模型Id删除
     *
     * @param modelId Long
     * @return int 0 - fail; 1 - success.
     */
    @Override
    public int delByModelId(Long modelId) {
        if (modelId == null) {
            return 0;
        }
        return modelMetaMapper.delByModelId(modelId);
    }
}
