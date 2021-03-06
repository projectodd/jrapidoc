package org.projectodd.jrapidoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used for indication whether parameter is required or
 * optional<br/>
 * <br/>
 * Can be placed on field, method parameter, constructor parameter ana setter
 * method
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocIsRequired {
    /**
     * true if required<br/>
     * false if optional<br/>
     * 
     * @return
     */
    boolean value() default true;
}
