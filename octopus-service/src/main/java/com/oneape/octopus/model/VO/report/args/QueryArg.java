package com.oneape.octopus.model.VO.report.args;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 17:21.
 * Modify:
 */
@Data
public class QueryArg implements Serializable {
    private String        name;
    private String        label;
    private boolean       required;
    private String        dataType;
    private List<String>  dependOnList;
    // Query component type
    private BaseComponent component;
}
