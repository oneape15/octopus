package com.oneape.octopus.domain.serve;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-10-26 11:13.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServeVersionDO extends BaseDO {
    /**
     * The unique code.
     */
    @EntityColumn(name = "version_code", nullable = false)
    private String versionCode;

    /**
     * Description version information.
     */
    @EntityColumn(name = "version_log", nullable = false)
    private String versionLog;

    /**
     * The version type
     * {@see VersionType}
     */
    @EntityColumn(name = "version_type", nullable = false)
    private String versionType;

    /**
     * save the serve config information.
     */
    @EntityColumn(name = "serve_config", bigColumn = true)
    private String serveConfig;

}