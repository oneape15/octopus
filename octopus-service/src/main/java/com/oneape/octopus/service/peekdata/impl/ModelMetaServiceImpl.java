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
     * 根据对象进行查询
     *
     * @param model ModelMetaDO
     * @return List
     */
    @Override
    public List<ModelMetaVO> find(ModelMetaDO model) {
        List<ModelMetaVO> vos = new ArrayList<>();
        List<ModelMetaDO> list = modelMetaMapper.list(model);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(m -> vos.add(ModelMetaVO.ofDO(m)));
        }
        return vos;
    }

    /**
     * 保存模型元素信息
     *
     * @param modelId Long
     * @param metas   List
     * @return int 0 - 失败; 1 - 成功;
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
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int insert(ModelMetaDO model) {
        Preconditions.checkNotNull(model, "新增数据对象为空");
        return modelMetaMapper.insert(model);
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int edit(ModelMetaDO model) {
        Preconditions.checkNotNull(model, "更新数据对象为空");

        return modelMetaMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int deleteById(ModelMetaDO model) {
        return modelMetaMapper.delete(model);
    }

    /**
     * 根据模型Id删除
     *
     * @param modelId Long
     * @return int 0 - 失败; 1 - 成功;
     */
    @Override
    public int delByModelId(Long modelId) {
        if (modelId == null) {
            return 0;
        }
        return modelMetaMapper.delByModelId(modelId);
    }
}
