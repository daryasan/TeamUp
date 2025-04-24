package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ChatException extends Exception {
    public ChatException() {
        super();
    }

    public ChatException(String message) {
        super(message);
    }

}
