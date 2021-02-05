package com.oneape.octopus.admin.model.vo.report.args;

import com.oneape.octopus.commons.enums.ComponentType;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 19:06.
 * Modify:
 */
@Data
public class BaseComponent implements Serializable {
    // The Component type.
    protected ComponentType type;
    // The default value.
    protected String        valDefault;
    // Prompt information
    protected String        placeholder;
    // Whether a parameter is required.
    protected boolean       required;

    public BaseComponent(ComponentType type) {
        this.type = type;
    }
}
