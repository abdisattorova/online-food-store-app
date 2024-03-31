package com.online.foodstore.exception;

import com.online.foodstore.model.dto.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.online.foodstore.utils.ErrorCodes.ERR_BAD_REQUEST;


@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    @ExceptionHandler(value = {GeneralApiException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public GenericResponse<?> handleGeneralApiException(GeneralApiException exception) {
        log.error("Handled Exception {0}", exception);
        return GenericResponse.error(exception.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponse<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Handled Exception {0}", ex);
        return GenericResponse.error(ERR_BAD_REQUEST, Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponse<?> handleValidationErrors(BindException ex) {
        log.error("Handled Exception {0}", ex);
        return GenericResponse.error(ERR_BAD_REQUEST, Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
    }

}