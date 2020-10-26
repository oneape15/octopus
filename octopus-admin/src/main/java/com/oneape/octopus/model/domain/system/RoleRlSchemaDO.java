package com.oneape.octopus.model.domain.system;

import com.oneape.octopus.model.domain.BaseDO;
import lombok.Data;

import javax.persistence.Column;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-06 19:02.
 * Modify:
 */
@Data
public class RoleRlSchemaDO extends BaseDO {
    @Column(name = "role_id")
    private Long   roleId;
    @Column(name = "datasource_id")
    private Long   datasourceId;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "expire_time")
    private Long   expireTime;
}
