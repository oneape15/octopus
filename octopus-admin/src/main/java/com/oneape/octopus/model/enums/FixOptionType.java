package com.oneape.octopus.model.enums;

import com.oneape.octopus.model.VO.TreeNodeVO;
import org.apache.commons.lang3.StringUtils;

/**
 * 固定的下拉框选择项
 */
public enum FixOptionType {
    NULL,
    ALL,
    DEFAULT;


    public static FixOptionType byName(String name) {
        if (StringUtils.isBlank(name)) {
            return DEFAULT;
        }

        for (FixOptionType fot : values()) {
            if (StringUtils.equalsIgnoreCase(name, fot.name())) {
                return fot;
            }
        }
        return DEFAULT;
    }

    public TreeNodeVO getNode() {
        switch (this) {
            case NULL:
                return new TreeNodeVO("0", "无", null);
            case ALL:
                return new TreeNodeVO("-1", "全部", null);
            case DEFAULT:
            default:
                return null;
        }
    }
}
