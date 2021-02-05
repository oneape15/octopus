package com.oneape.octopus.admin.model.vo.report.args;

import com.oneape.octopus.commons.enums.ComponentType;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-24 10:05.
 * Modify:
 */
public class RadioComponent extends SelectorComponent {

    public RadioComponent() {
        setMulti(0);
        setType(ComponentType.RADIO);
    }
}
