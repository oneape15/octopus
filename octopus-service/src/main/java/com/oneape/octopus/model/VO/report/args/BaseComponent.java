package com.oneape.octopus.model.VO.report.args;

import com.oneape.octopus.model.enums.ComponentType;
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
    private ComponentType type;
    // The default value.
    private String        defaultValue;
    // Prompt information
    private String        placeholder;
    // Whether a parameter is required.
    private boolean       required;

    public BaseComponent(ComponentType type) {
        this.type = type;
    }
}
