package com.oneape.octopus.parse.data;

import com.oneape.octopus.commons.dto.Value;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Parse the result of a successful DSL.
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-08 15:41.
 * Modify:
 */
@Data
public class ParseResult implements Serializable {
    // The sql of parse.
    private String rawSql;
    // Replace  in rawSql the '?' char, List of values.
    private List<Value> values;
    // The alias for the field name is null by default.
    private Map<String, String> field2Alias;
}
