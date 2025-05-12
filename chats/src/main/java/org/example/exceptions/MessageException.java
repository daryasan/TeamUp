package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.BAD_REQUEST)
public class MessageException extends Exception{
    public MessageException(String message) {
        super(message);
    }
}
