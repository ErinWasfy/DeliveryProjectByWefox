package com.wefox.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPaymentDataException extends RuntimeException{

    public InvalidPaymentDataException(String message)
    {
        super(message);
    }
}
