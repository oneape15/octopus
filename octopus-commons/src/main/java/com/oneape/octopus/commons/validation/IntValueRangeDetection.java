package com.oneape.octopus.commons.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-14 17:36.
 * Modify:
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IntValueRangeDetectionValidator.class})
@Documented
public @interface IntValueRangeDetection {
    String message() default "The value entered is not in the range of valid values.";

    int[] range() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
