package org.projectodd.jrapidoc.model.handler;

/**
 * Created by Tomas "sarzwest" Jiricek on 12.4.15.
 */
public class HandlerException extends Exception{

    Action behaviour;

    public HandlerException(Action behaviour) {
        this.behaviour = behaviour;
    }

    public HandlerException(String message, Action behaviour) {
        super(message);
        this.behaviour = behaviour;
    }

    public HandlerException(String message, Throwable cause, Action behaviour) {
        super(message, cause);
        this.behaviour = behaviour;
    }

    public HandlerException(Throwable cause, Action behaviour) {
        super(cause);
        this.behaviour = behaviour;
    }

    public HandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Action behaviour) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.behaviour = behaviour;
    }

    public Action getBehaviour() {
        return behaviour;
    }

    public enum Action {
        STOP_HANDLER_CURRENT, STOP_HANDLERS, FAILURE_EXCEPTION, EXECUTION_EXCEPTION
    }
}
