package com.oneape.octopus.admin.service.warehouse;

import com.oneape.octopus.admin.service.BaseService;
import com.oneape.octopus.domain.warehouse.DdlAuditInfoDO;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 16:13.
 * Modify:
 */
public interface DdlAuditService extends BaseService<DdlAuditInfoDO> {

    /**
     * Execute DDL statements
     *
     * @param ddlId Long
     * @return int 1 - success; 0 - fail.
     */
    int exec(Long ddlId);

    /**
     * Review DDL statements
     *
     * @param model DdlAuditInfoDO
     * @return int 1 - success; 0 - fail.
     */
    int audit(DdlAuditInfoDO model);
}
