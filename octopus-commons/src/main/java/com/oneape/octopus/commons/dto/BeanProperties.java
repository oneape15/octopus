package com.oneape.octopus.commons.dto;

import lombok.Data;

@Data
public class BeanProperties {
    /**
     * 属性名
     */
    private String name;
    /**
     * 属性类型
     */
    private String type;
    /**
     * 属性值
     */
    private Object value;
    /**
     * 是否为表字段
     */
    private Boolean dbColumn = false;
    /**
     * 是否可为空
     */
    private Boolean nullAble;
    /**
     * 对应表字段名称
     */
    private String dbColumnName;
}
