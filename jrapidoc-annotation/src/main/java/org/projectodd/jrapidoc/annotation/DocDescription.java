package org.projectodd.jrapidoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation adding description support for:<br/>
 * <ul>
 *     <li>JAX-RS API
 *         <ul>
 *             <li>Resource class</li>
 *             <li>Resource method</li>
 *             <li>Resource class constructor parameter</li>
 *             <li>Resource class attribute</li>
 *             <li>Resource class attribute setter method</li>
 *             <li>Resource method parameter</li>
 *         </ul>
 *     </li>
 *     <li>JAX-WS API
 *         <ul>
 *             <li>SEI class</li>
 *             <li>service method</li>
 *             <li>service method parameter</li>
 *         </ul>
 *     </li>
 * </ul>
 * Created by Tomas "sarzwest" Jiricek on 15.12.14.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocDescription {
    String value();
}
