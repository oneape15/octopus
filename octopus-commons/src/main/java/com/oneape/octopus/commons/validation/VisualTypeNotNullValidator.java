package com.oneape.octopus.commons.validation;

import com.oneape.octopus.commons.enums.VisualType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 10:35.
 * Modify:
 */
public class VisualTypeNotNullValidator implements ConstraintValidator<VisualTypeNotNull, Integer> {

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null || value < 0) return false;

        return VisualType.isValid(value);
    }
}
