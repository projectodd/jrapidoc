package org.projectodd.jrapidoc.annotation.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotatation can be used on resource class and resource method<br/>
 * Because javax.ws.rs.Path.value can contain regular
 * expressions which are not much readable, here can be placed example of path.<br/>
 * <br/>
 * Created by Tomas "sarzwest" Jiricek on 14.2.15.<br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocPathExample {
    /**
     * Because javax.ws.rs.Path.value can contain regular
     * expressions which are not much readable, here can be placed example of path.
     *
     * @return
     */
    String value();
}
