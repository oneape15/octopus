package com.oneape.octopus.model.VO.report.args;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 17:21.
 * Modify:
 */
@Data
public class QueryArg implements Serializable {
    private String        label;
    private String        name;
    private boolean       required;
    private String        dataType;
    // Query component type
    private BaseComponent component;
}
