package org.projectodd.jrapidoc.exception;

/**
 * Exception mapped to {@link org.apache.maven.plugin.MojoExecutionException}
 */
public class JrapidocExecutionException extends Exception {
    
    private static final long serialVersionUID = 2755515091279512582L;

    public JrapidocExecutionException() {
    }

    public JrapidocExecutionException(String message) {
        super(message);
    }

    public JrapidocExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JrapidocExecutionException(Throwable cause) {
        super(cause);
    }

    public JrapidocExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
