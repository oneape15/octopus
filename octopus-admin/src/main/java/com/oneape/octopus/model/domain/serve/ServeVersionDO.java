package com.oneape.octopus.model.domain.serve;

import com.oneape.octopus.model.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

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
    @Column(name = "version_code", nullable = false)
    private String versionCode;

    /**
     * Description version information.
     */
    @Column(name = "version_log", nullable = false)
    private String versionLog;

    /**
     * The version type
     * {@see VersionType}
     */
    @Column(name = "version_type", nullable = false)
    private String versionType;

    /**
     * save the serve config information.
     */
    @Column(name = "serve_config")
    private String serveConfig;

}
