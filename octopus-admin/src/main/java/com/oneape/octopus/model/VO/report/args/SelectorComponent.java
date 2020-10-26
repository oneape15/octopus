package com.oneape.octopus.model.VO.report.args;

import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.commons.value.TitleValueDTO;
import com.oneape.octopus.model.enums.ComponentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 19:18.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SelectorComponent extends BaseComponent {
    /**
     * Support to select more than one at a time.
     * 0 - single; 1 - multi;
     */
    private int                 multi;
    /**
     * KV list to be selected.
     */
    private List<TitleValueDTO> values;

    public SelectorComponent() {
        super(ComponentType.SELECTOR);
    }
}