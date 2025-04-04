package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ReviewException extends Exception {
    public ReviewException() {
        super();
    }

    public ReviewException(String message) {
        super(message);
    }

}
