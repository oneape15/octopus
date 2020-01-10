package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * 数据源DO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DatasourceDO extends BaseDO {
    /**
     * 数据库名称
     */
    private String name;
    /**
     * 数据源类型 MySQL, Oracle
     */
    private String type;
    /**
     * 状态, 0 - 可用; 1 - 不可用
     */
    private Integer status;
    /**
     * 数据源地址
     */
    @Column(name = "jdbc_url")
    private String jdbcUrl;
    /**
     * jdbc驱动
     */
    @Column(name = "jdbc_driver")
    private String jdbcDriver;
    /**
     * 数据源用户名
     */
    private String username;
    /**
     * 数据源登录密码
     */
    private String password;
    /**
     * 连接池超时时间(ms)
     */
    private Integer timeout;
    /**
     * 检测SQL
     */
    @Column(name = "test_sql")
    private String testSql;
    /**
     * 描述
     */
    private String comment;

    public DatasourceDO(String name) {
        this.name = name;
    }
}
