package com.oneape.octopus.commons.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Value implements Cloneable, Serializable {
    /**
     * The value name
     */
    private String name;
    /**
     * The data type
     */
    private DataType dataType;
    /**
     * The specific value
     */
    private Object value;
    /**
     * Whether to scope query
     */
    private boolean isRange;
    /**
     * Is it a multi-valued query
     */
    private boolean isMulti;

    public Value(Object value, DataType dt) {
        this.value = value;
        this.dataType = dt;
    }

    @Override
    public Value clone() {
        try {
            return (Value) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
