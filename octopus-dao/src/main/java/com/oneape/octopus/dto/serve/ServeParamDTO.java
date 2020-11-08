package com.oneape.octopus.dto.serve;

import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.commons.enums.ComponentType;
import com.oneape.octopus.commons.enums.ReportParamType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Serve query parameter information DTO.
 */
@Data
@NoArgsConstructor
public class ServeParamDTO {
    /**
     * The serve param name.
     */
    private String  name;
    /**
     * The serve param alias name.
     */
    private String  alias;
    /**
     * {@link DataType }
     * The serve param data type.
     */
    private String  dataType;
    /**
     * default value
     */
    private String  valDefault;
    /**
     * The maximum value
     */
    private String  valMax;
    /**
     * The minimum value.
     */
    private String  valMin;
    /**
     * Prohibited value.
     */
    private String  valForbidden;
    /**
     * Is a required field 0 - no; 1 - yes.
     */
    private Integer required;
    /**
     * Parameters briefly describe information.
     */
    private String  placeholder;
    /**
     * Dependency, taking the name value, multiple separated by commas.
     */
    private String  dependOn;
    /**
     * The parameter types; 0 - normal; 1 - inline; 2 - between; 4 - multi;
     * {@link ReportParamType}
     */
    private Integer type;
    /**
     * When the field value content depends on another SQL query result (LOV), fill in.
     */
    private Long    lovReportId;
    /**
     * The parameter component type.
     * {@link ComponentType}
     */
    private String  componentType;
    /**
     * The query parameter LOV is the specified KV name.
     * eg: kname,vname
     */
    private String  lovKvName;

}
