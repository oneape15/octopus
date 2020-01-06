package com.oneape.octopus.datasource;

import lombok.Data;

import java.io.Serializable;

@Data
public class DatasourceInfo implements Serializable {
    /**
     * 数据源类型
     */
    private DatasourceTypeHelper datasourceType;
    /**
     * DB url
     */
    private String url;
    /**
     * 登录名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
