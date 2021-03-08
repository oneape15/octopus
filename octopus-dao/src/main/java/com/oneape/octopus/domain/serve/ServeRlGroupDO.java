package com.oneape.octopus.domain.serve;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 14:35.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServeRlGroupDO extends BaseDO {
    @EntityColumn(name = "serve_id")
    private Long serveId;
    @EntityColumn(name = "group_id")
    private Long groupId;

    public ServeRlGroupDO(Long serveId, Long groupId) {
        this.serveId = serveId;
        this.groupId = groupId;
    }
}
