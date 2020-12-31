package com.oneape.octopus.commons.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 10:34.
 * Modify:
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {VisualTypeNotNullValidator.class})
@Documented
public @interface VisualTypeNotNull {
    String message() default "Enter the enumeration type in VisualType class.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
