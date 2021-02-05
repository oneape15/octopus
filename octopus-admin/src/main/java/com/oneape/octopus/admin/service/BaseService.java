package com.oneape.octopus.admin.service;

import com.oneape.octopus.domain.BaseDO;

import java.util.List;

public interface BaseService<T extends BaseDO> {

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    int save(T model);

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    int deleteById(Long id);

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    T findById(Long id);


    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    List<T> find(T model);

}
