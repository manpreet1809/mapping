package com.mhp.coding.challenges.mapping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Manpreet Kaur
 */
@ControllerAdvice
public class ArticleHandler {
    @ExceptionHandler(value = ArticleIdNotFound.class)
    public ResponseEntity<CustomErrorMessage> entityNotFoundExceptionHandler(ArticleIdNotFound exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage(message));
    }
}
