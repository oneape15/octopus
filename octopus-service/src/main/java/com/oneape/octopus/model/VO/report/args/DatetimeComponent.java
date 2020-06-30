package com.oneape.octopus.model.VO.report.args;

import com.oneape.octopus.model.enums.ComponentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-24 10:10.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatetimeComponent extends InputComponent {

    /**
     * the time format.
     * yyyy-MM-dd HH:mm:ss
     * yyyy-MM-dd
     * yyyy-MM
     * yyyy
     * HH:mm
     * HH
     * WEEK
     */
    private String format;

    public DatetimeComponent() {
        setType(ComponentType.DATETIME);
    }
}
