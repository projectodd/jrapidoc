package org.projectodd.jrapidoc.exception;

/**
 * Exception mapped to {@link org.apache.maven.plugin.MojoExecutionException}
 *
 * Created by papa on 25.4.15.
 */
public class JrapidocExecutionException extends Exception {
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
