package com.oneape.octopus.model.DO;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * 数据源DO
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DatasourceDO extends BaseDO {
    /**
     * 数据库别名
     */
    private String nickname;
    /**
     * 数据源地址
     */
    private String url;
    /**
     * 驱动class名称
     */
    @Column(name = "driver_class")
    private String driverClass;
    /**
     * 数据源用户名
     */
    private String username;
    /**
     * 数据源登录密码
     */
    private String password;
    /**
     * 描述
     */
    private String comment;
}
