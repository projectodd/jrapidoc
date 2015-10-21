package org.projectodd.jrapidoc.annotation.soap.wsprovider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
public @interface DocSOAPBinding
{
    Style style() default Style.DOCUMENT;

    Use use() default Use.LITERAL;

    ParameterStyle parameterStyle() default ParameterStyle.WRAPPED;

    public enum Style{
        DOCUMENT,  RPC;
    }

    public enum Use{
        LITERAL,  ENCODED;
    }

    public enum ParameterStyle{
        BARE,  WRAPPED;
    }
}
