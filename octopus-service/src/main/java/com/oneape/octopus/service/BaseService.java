package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.BaseDO;

public interface BaseService<T extends BaseDO> {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    int insert(T model);

    /**
     * Modify the data.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    int edit(T model);

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    int deleteById(T model);
}
