package com.oneape.octopus.commons.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Refer to the javax.persistence.Column class.
 * Created by oneape<oneape15@163.com>
 * Created 2020-10-27 16:48.
 * Modify:
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface EntityColumn {
    /**
     * (Optional) The name of the column. Defaults to
     * the property or field name.
     */
    String name() default "";

    /**
     * (Optional) Whether the property is a unique key.  This is a
     * shortcut for the UniqueConstraint annotation at the table
     * level and is useful for when the unique key constraint is
     * only a single field. This constraint applies in addition
     * to any constraint entailed by primary key mapping and
     * to constraints specified at the table level.
     */
    boolean unique() default false;

    /**
     * (Optional) Whether the database column is nullable.
     */
    boolean nullable() default true;

    /**
     * (Optional) Whether the database column is big column.
     * ( TEXT, BLOB eg.)
     */
    boolean bigColumn() default false;

    /**
     * Whether the database column
     */
    boolean dbColumn() default true;

    /**
     * (Optional) The name of the table that contains the column.
     * If absent the column is assumed to be in the primary table.
     */
    String table() default "";

    /**
     * (Optional) The column length. (Applies only if a
     * string-valued column is used.)
     */
    int length() default 255;

    /**
     * (Optional) The precision for a decimal (exact numeric)
     * column. (Applies only if a decimal column is used.)
     * Value must be set by developer if used when generating
     * the DDL for the column.
     */
    int precision() default 0;

    /**
     * (Optional) The scale for a decimal (exact numeric) column.
     * (Applies only if a decimal column is used.)
     */
    int scale() default 0;
}
