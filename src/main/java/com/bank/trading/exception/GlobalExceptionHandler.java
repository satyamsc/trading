package com.bank.trading.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
/**
 * GlobalExceptionHandler is a controller advice class that handles exceptions globally for the trading application.
 * It provides exception handling methods for specific exception types and general exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * The key used for storing the error message in the error map.
     */
    public static final String ERROR_MESSAGE = "errorMessage";

    /**
     * Handles the ResourceNotFoundException and returns a JSON map containing the error message.
     *
     * @param ex The ResourceNotFoundException that was thrown.
     * @return A JSON map containing the error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handleBusinessException(ResourceNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR_MESSAGE, ex.getMessage());
        return errorMap;
    }

    /**
     * Handles generic Exception and returns a JSON map containing the error message.
     *
     * @param ex The Exception that was thrown.
     * @return A JSON map containing the error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleBusinessException(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERROR_MESSAGE, ex.getMessage());
        return errorMap;
    }
}
