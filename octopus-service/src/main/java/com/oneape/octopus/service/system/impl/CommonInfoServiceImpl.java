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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommonInfoServiceImpl implements CommonInfoService {

    @Resource
    private CommonInfoMapper commonInfoMapper;

    /**
     * Query against an object.
     *
     * @param commonInfo CommonInfoDO
     * @return List
     */
    @Override
    public List<CommonInfoDO> find(CommonInfoDO commonInfo) {
        if (commonInfo == null) {
            commonInfo = new CommonInfoDO();
        }
        return commonInfoMapper.list(commonInfo);
    }

    /**
     * Get all classified information.
     *
     * @return List
     */
    @Override
    public List<String> getAllClassify() {
        return commonInfoMapper.getAllClassify();
    }

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int save(CommonInfoDO model) {
        Preconditions.checkNotNull(model, "The Object is null.");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getClassify(), model.getKey(), model.getValue()),
                "The classify, key or value is null.");

        if (model.getParentId() == null || model.getParentId() < GlobalConstant.DEFAULT_VALUE) {
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        } else {
            Preconditions.checkNotNull(commonInfoMapper.findById(model.getParentId()), "The parent node information does not exist.");
        }

        Preconditions.checkArgument(
                commonInfoMapper.getSameBy(model.getClassify(), model.getKey(), model.getId()) == 0,
                "The same key exists under the same classification."
        );

        if (model.getId() != null) {
            Preconditions.checkNotNull(commonInfoMapper.findById(model.getId()), "The node information does not exist.");
            return commonInfoMapper.update(model);
        }

        return commonInfoMapper.insert(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(CommonInfoDO model) {
        Preconditions.checkNotNull(model, "The Object is null.");
        Preconditions.checkNotNull(model.getId(), "The primary key is null.");
        CommonInfoDO tmp = new CommonInfoDO();
        tmp.setParentId(model.getId());
        int childrenSize = commonInfoMapper.size(tmp);
        if (childrenSize > 0) {
            throw new BizException("There are also child nodes that are not allowed to be deleted.");
        }
        return commonInfoMapper.delete(model);
    }
}
