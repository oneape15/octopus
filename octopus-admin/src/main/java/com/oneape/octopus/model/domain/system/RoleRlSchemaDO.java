package com.oneape.octopus.model.domain.system;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.model.domain.BaseDO;
import lombok.Data;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-06 19:02.
 * Modify:
 */
@Data
public class RoleRlSchemaDO extends BaseDO {
    @EntityColumn(name = "role_id")
    private Long   roleId;
    @EntityColumn(name = "datasource_id")
    private Long   datasourceId;
    @EntityColumn(name = "table_name")
    private String tableName;
    @EntityColumn(name = "expire_time")
    private Long   expireTime;
}
