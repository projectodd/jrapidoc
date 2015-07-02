package org.projectodd.jrapidoc.exception;

/**
 * Exception mapped to {@link org.apache.maven.plugin.MojoFailureException}
 *
 * Created by papa on 25.4.15.
 */
public class JrapidocFailureException extends Exception {
    public JrapidocFailureException() {
    }

    public JrapidocFailureException(String message) {
        super(message);
    }

    public JrapidocFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public JrapidocFailureException(Throwable cause) {
        super(cause);
    }

    public JrapidocFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
