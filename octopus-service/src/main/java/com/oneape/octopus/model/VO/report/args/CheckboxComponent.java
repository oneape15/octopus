package com.oneape.octopus.model.VO.report.args;

import com.oneape.octopus.model.enums.ComponentType;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-24 10:05.
 * Modify:
 */
public class CheckboxComponent extends SelectorComponent {

    public CheckboxComponent() {
        setMulti(1);
        setType(ComponentType.CHECK_BOX);
    }
}
