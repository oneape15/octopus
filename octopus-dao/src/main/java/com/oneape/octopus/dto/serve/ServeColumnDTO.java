package com.oneape.octopus.dto.serve;

import com.oneape.octopus.commons.dto.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Serve field information DTO.
 */
@Data
@NoArgsConstructor
public class ServeColumnDTO implements Serializable {

    /**
     * The serve column name.
     */
    private String  name;
    /**
     * The serve column alias name.
     */
    private String  alias;
    /**
     * {@link DataType}
     * The serve column data type.
     */
    private String  dataType;
    /**
     * data unit
     */
    private String  unit;
    /**
     * Where does the unit symbol apply. 0 - the table head; 1 - each row item.
     */
    private Integer unitUseWhere;
    /**
     * The default value when the data item is empty.
     */
    private String  defaultValue;
    /**
     * Is a hidden column; 0 - no; 1 - yes.
     */
    private Integer hidden;
    /**
     * Type of resource to jump when drilling down a column.
     * eg: 1 - report; 2 - dashboard; 3 - External links.
     */
    private Integer drillSourceType;
    /**
     * Drill down to the resource URI.
     */
    private String  drillUri;
    /**
     * The parameters required when drilling down the column;
     * eg: kv1=column_name1; kv2=column_name2;
     */
    private String  drillParams;
    /**
     * When drilling down the column, open mode.
     * 1 - new window; 2 - Current window; 3 - Popover page.
     */
    private Integer drillOpenType;
    /**
     * Is a frozen column; 0 - no; 1 - yes
     */
    private Integer frozen;
    /**
     * Whether sorting is supported; 0 - no; 1 - yes
     */
    private Integer supportSort;
    /**
     * Data formatting macro
     */
    private String  formatMacro;
    /**
     * description
     */
    private String  comment;
}
