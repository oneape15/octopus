package com.oneape.octopus.domain.serve;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.commons.enums.VersionType;
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
     * The serve id.
     */
    @EntityColumn(name = "serve_id", nullable = false)
    private Long   serveId;
    /**
     * The unique code.
     */
    @EntityColumn(name = "version_code", nullable = false)
    private String versionCode;

    /**
     * The version type
     * {@link VersionType}
     */
    @EntityColumn(name = "version_type", nullable = false)
    private String versionType;

    /**
     * save the serve config information.
     */
    @EntityColumn(name = "serve_config", bigColumn = true)
    private String serveConfig;

    /**
     * Description version information.
     */
    private String comment;

}
