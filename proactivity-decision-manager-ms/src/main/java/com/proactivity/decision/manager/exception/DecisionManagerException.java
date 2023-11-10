package com.proactivity.decision.manager.exception;

public class DecisionManagerException extends RuntimeException{

    private static final long serialVersionUID = 6899646247926838143L;

	public DecisionManagerException() {
    }

    public DecisionManagerException(String message) {
        super(message);
    }

    public DecisionManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecisionManagerException(Throwable cause) {
        super(cause);
    }

    public DecisionManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
