package com.oneape.octopus.commons.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-04-06 4:43 PM.
 * Modify:
 */
@Data
public class TabulationCell {
    // The cell value.
    private Object value;
    // The cell attribute.
    private Map<String, Object> props;

    public TabulationCell setPropKV(String key, Object value) {
        if (StringUtils.isBlank(key)) return this;

        if (props == null) {
            props = new HashMap<>();
        }
        props.put(key, value);

        return this;
    }
}
