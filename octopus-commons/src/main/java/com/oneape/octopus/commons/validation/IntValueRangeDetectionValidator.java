package com.oneape.octopus.commons.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-14 17:36.
 * Modify:
 */
public class IntValueRangeDetectionValidator implements ConstraintValidator<IntValueRangeDetection, Integer> {
    private List<Integer> validRanges;

    @Override
    public void initialize(IntValueRangeDetection cAnn) {
        validRanges = new ArrayList<>();
        for (int tmp : cAnn.range()) {
            validRanges.add(tmp);
        }

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && validRanges.contains(value);
    }
}
