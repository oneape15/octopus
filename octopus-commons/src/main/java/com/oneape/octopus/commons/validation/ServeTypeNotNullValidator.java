package com.oneape.octopus.commons.validation;

import com.oneape.octopus.commons.enums.ServeType;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 10:20.
 * Modify:
 */
public class ServeTypeNotNullValidator implements ConstraintValidator<ServeTypeNotNull, String> {
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
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        return ServeType.isValid(value);
    }

}
