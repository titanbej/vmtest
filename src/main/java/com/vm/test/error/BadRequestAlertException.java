package com.vm.test.error;

public class BadRequestAlertException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public BadRequestAlertException(String defaultMessage) {
        super(defaultMessage);
    }

    
}
