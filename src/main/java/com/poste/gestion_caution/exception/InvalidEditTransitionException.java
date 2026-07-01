package com.poste.gestion_caution.exception;

public class InvalidEditTransitionException extends RuntimeException {

    private final Long cautionId;

    public InvalidEditTransitionException(String message, Long cautionId) {
        super(message);
        this.cautionId = cautionId;
    }

    public Long getCautionId() {
        return cautionId;
    }
}
