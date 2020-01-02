package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.BaseDO;

public interface BaseService<T extends BaseDO> {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    int insert(T model);

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    int edit(T model);

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    int deleteById(T model);
}
