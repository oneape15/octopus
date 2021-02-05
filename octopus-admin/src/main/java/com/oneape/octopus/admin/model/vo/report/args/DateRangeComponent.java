package com.oneape.octopus.admin.model.vo.report.args;

import com.oneape.octopus.commons.enums.ComponentType;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-24 10:17.
 * Modify:
 */
public class DateRangeComponent extends DatetimeComponent {

    public DateRangeComponent() {
        setType(ComponentType.DATE_RANGE);
    }
}
