package com.oneape.octopus.model.VO.report.args;

import com.oneape.octopus.model.DTO.report.ReportParamDTO;
import com.oneape.octopus.model.enums.ComponentType;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-24 11:32.
 * Modify:
 */
public class ComponentFactory {

    public static BaseComponent build(ReportParamDTO dto) {
        BaseComponent component = null;

        ComponentType ct = ComponentType.getByName(dto.getComponentType());
        if (ct == null) {
            ct = ComponentType.INPUT;
        }

        switch (ct) {
            case INPUT:
                component = buildInput(dto);
                break;
            case INPUT_RANGE:
                break;
            case SWITCH:
                component = buildSwitch(dto);
                break;
            case RADIO:
                component = buildRadio(dto);
                break;
            case CHECK_BOX:
                component = buildCheckbox(dto);
                break;
            case SELECTOR:
                component = buildSelector(dto);
                break;
            case DATETIME:
                component = buildDatetime(dto);
                break;
            case DATE_RANGE:
                component = buildDateRange(dto);
                break;
            default:
                throw new RuntimeException("Impossible error.");
        }

        return component;
    }

    public static InputComponent buildInput(ReportParamDTO dto) {
        InputComponent com = new InputComponent();
        com.setValMin(dto.getValMin());
        com.setValMax(dto.getValMax());
        com.setValForbidden(dto.getValForbidden());
        com.setValDefault(dto.getValDefault());
        return com;
    }

    public static SwitchComponent buildSwitch(ReportParamDTO dto) {
        SwitchComponent com = new SwitchComponent();
        com.setValDefault(dto.getValDefault());
        com.setValues(dto.getValues());
        return com;
    }

    public static RadioComponent buildRadio(ReportParamDTO dto) {
        RadioComponent com = new RadioComponent();
        com.setValDefault(dto.getValDefault());
        com.setValues(dto.getValues());
        return com;
    }

    public static CheckboxComponent buildCheckbox(ReportParamDTO dto) {
        CheckboxComponent com = new CheckboxComponent();
        com.setValDefault(dto.getValDefault());
        com.setValues(dto.getValues());
        return com;
    }

    public static SelectorComponent buildSelector(ReportParamDTO dto) {
        SelectorComponent com = new SelectorComponent();
        com.setValDefault(dto.getValDefault());
        com.setValues(dto.getValues());
        return com;
    }

    public static DatetimeComponent buildDatetime(ReportParamDTO dto) {
        DatetimeComponent com = new DatetimeComponent();
        com.setValDefault(dto.getValDefault());

        return com;
    }


    public static DateRangeComponent buildDateRange(ReportParamDTO dto) {
        DateRangeComponent com = new DateRangeComponent();
        com.setValDefault(dto.getValDefault());
        return com;
    }

}
