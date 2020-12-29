package com.oneape.octopus.service.serve.impl;

import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.mapper.serve.ServeGroupMapper;
import com.oneape.octopus.service.serve.ServeGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 16:39.
 * Modify:
 */
@Slf4j
@Service
public class ServeGroupServiceImpl implements ServeGroupService {
    @Resource
    private ServeGroupMapper serveGroupMapper;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(ServeGroupDO model) {
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
        return 0;
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public ServeGroupDO findById(Long id) {
        return null;
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<ServeGroupDO> find(ServeGroupDO model) {
        return null;
    }

    /**
     * check has same name.
     *
     * @param name     String
     * @param filterId Long
     * @return boolean true - has, false - no
     */
    @Override
    public boolean hasSameName(String name, Long filterId) {
        int count = serveGroupMapper.checkHasTheSameName(name, filterId);
        return count > 0;
    }
}
