package com.oneape.octopus.model.VO.report.args;

import com.oneape.octopus.model.enums.ComponentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 19:14.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputComponent extends BaseComponent {
    private String valMin;
    private String ValMax;
    private String valForbidden;

    public InputComponent() {
        super(ComponentType.INPUT);
    }
}
