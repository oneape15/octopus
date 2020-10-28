package com.oneape.octopus.datasource;

import lombok.Data;

import java.io.Serializable;

@Data
public class DatasourceInfo implements Serializable {
    /**
     * the datasource primary key
     */
    private Long                 id;
    /**
     * 数据源类型
     */
    private DatasourceTypeHelper datasourceType;
    /**
     * DB url
     */
    private String               url;
    /**
     * 登录名
     */
    private String               username;
    /**
     * 密码
     */
    private String               password;

    /**
     * 检测SQL
     */
    private String testSql;
}
