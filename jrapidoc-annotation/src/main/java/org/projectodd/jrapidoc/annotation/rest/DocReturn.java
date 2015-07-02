package org.projectodd.jrapidoc.annotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used on resource methods for customizing return option in documentation.<br/>
 * <br/>
 * Created by Tomas "sarzwest" Jiricek on 4.1.15.<br/>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocReturn {

    /**
     * Http status returned
     * @return
     */
    int http();

    /**
     * Http header names returned
     * @return
     */
    String[] headers() default {};

    /**
     * Http cookie names returned
     * @return
     */
    String[] cookies() default {};

    /**
     * Java type returned
     * @return
     */
    Class<?> type() default Void.class;

    /**
     * Wrapper for {@link DocReturn#type()}.
     * @return
     */
    Structure structure() default Structure.OBJECT;

    /**
     * Description of return option
     * @return
     */
    String description() default "";

    /**
     * Description of returned type (set in {@link DocReturn#type()})
     * @return
     */
    String typeDescription() default "";

    enum Structure {
        /**
         * {@link DocReturn#type()} is not wrapped
         */
        OBJECT,
        /**
         * {@link DocReturn#type()} is wrapped into array
         */
        ARRAY,
        /**
         * {@link DocReturn#type()} is wrapped into map as value type (key type is string)
         */
        MAP
    }
}
