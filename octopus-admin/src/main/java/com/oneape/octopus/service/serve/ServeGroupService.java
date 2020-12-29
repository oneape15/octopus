package com.oneape.octopus.service.serve;

import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.service.BaseService;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 16:15.
 * Modify:
 */
public interface ServeGroupService extends BaseService<ServeGroupDO> {

    /**
     * check has same name.
     *
     * @param name     String
     * @param filterId Long
     * @return boolean true - has, false - no
     */
    boolean hasSameName(String name, Long filterId);
}
