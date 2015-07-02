package org.projectodd.jrapidoc.model;

/**
 * Created by Tomas "sarzwest" Jiricek on 11.4.15.
 */
public class SoapBinding {

    private String style;
    private String use;
    private String parameterStyle;

    private SoapBinding(String style, String use, String parameterStyle) {
        this.style = style;
        this.use = use;
        this.parameterStyle = parameterStyle;
    }

    public String getStyle() {
        return style;
    }

    public String getUse() {
        return use;
    }

    public String getParameterStyle() {
        return parameterStyle;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public void setParameterStyle(String parameterStyle) {
        this.parameterStyle = parameterStyle;
    }

    public static class SoapBindingBuilder{

        private String style = "DOCUMENT";
        private String use = "LITERAL";
        private String parameterStyle = "WRAPPED";

        public SoapBindingBuilder style(String style) {
            this.style = style;
            return this;
        }

        public SoapBindingBuilder use(String use) {
            this.use = use;
            return this;
        }

        public SoapBindingBuilder parameterStyle(String parameterStyle) {
            this.parameterStyle = parameterStyle;
            return this;
        }

        public SoapBinding build(){
            return new SoapBinding(style, use, parameterStyle);
        }

    }
}
