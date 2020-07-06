package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.BaseDO;

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
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    int deleteById(T model);
}
