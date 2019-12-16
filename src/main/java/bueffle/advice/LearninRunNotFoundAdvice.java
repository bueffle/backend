package bueffle.advice;

import bueffle.exception.LearningRunNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class LearninRunNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(LearningRunNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String cardNotFoundHandler(LearningRunNotFoundException ex) {
        return ex.getMessage();
    }
}
