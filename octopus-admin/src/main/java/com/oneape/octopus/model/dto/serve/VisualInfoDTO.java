package com.oneape.octopus.model.dto.serve;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-10-21 11:04.
 * Modify:
 */
@Data
@NoArgsConstructor
public class VisualInfoDTO {
    /**
     * When the chart is displayed, the X-axis column name; Multiple with ";" separated.
     */
    private String  xAxis;
    /**
     * When the chart is displayed, the Y-axis column name; Multiple with ";" separated'
     */
    private String  yAxis;
    /**
     * Query the field label display length
     */
    private Integer paramLabelLen;
    /**
     * Query the field control display length
     */
    private Integer paramMediaLen;
}
