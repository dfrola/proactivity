package com.proactivity.groovy.exception;

public class GroovyInvocationException extends RuntimeException{

    private static final long serialVersionUID = 6899646247926838143L;

	public GroovyInvocationException() {
    }

    public GroovyInvocationException(String message) {
        super(message);
    }

    public GroovyInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GroovyInvocationException(Throwable cause) {
        super(cause);
    }

    public GroovyInvocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
