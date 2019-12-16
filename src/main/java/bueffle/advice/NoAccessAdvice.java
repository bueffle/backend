package bueffle.advice;

import bueffle.exception.NoAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class NoAccessAdvice {

    @ResponseBody
    @ExceptionHandler(NoAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String cardNotFoundHandler(NoAccessException ex) {
        return ex.getMessage();
    }
}
