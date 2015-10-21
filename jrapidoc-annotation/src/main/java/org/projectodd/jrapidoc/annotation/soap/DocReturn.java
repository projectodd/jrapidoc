package org.projectodd.jrapidoc.annotation.soap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used on web service method<br/>
 * It is used for customizing Java return type<br/>
 * <br/>
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocReturn {

    public static int HTTP_STATUS_DEFAULT = 200;
    public static int HTTP_STATUS_FAULT = 500;
    /**
     * http status of returned message
     * 
     * @return
     */
    int http() default HTTP_STATUS_DEFAULT;

    /**
     * @WebResult Java type from method signature
     * @return
     */
    Class<?> type() default Void.class;

    /**
     * Description of return option
     * 
     * @return
     */
    String description() default "";

    /**
     * Description of return type
     * 
     * @return
     */
    String typeDescription() default "";
}
