package com.proactivity.groovy.exception;

public class DecisionManagerPathNotFoudException extends RuntimeException{

    private static final long serialVersionUID = 6899646247926838143L;

	public DecisionManagerPathNotFoudException() {
    }

    public DecisionManagerPathNotFoudException(String message) {
        super(message);
    }

    public DecisionManagerPathNotFoudException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecisionManagerPathNotFoudException(Throwable cause) {
        super(cause);
    }

    public DecisionManagerPathNotFoudException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
