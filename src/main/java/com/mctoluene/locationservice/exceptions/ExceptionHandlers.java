package com.mctoluene.locationservice.exceptions;

import com.mctoluene.locationservice.services.internal.MessageSourceService;
import com.sabiam.commons.exceptions.ConflictException;
import com.sabiam.commons.exceptions.NotFoundException;
import com.sabiam.commons.exceptions.ValidatorException;
import com.sabiam.commons.response.AppResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlers {
    private final MessageSourceService messageSourceService;

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public AppResponse<Object> handleModelAlreadyExistException(final ConflictException ex) {
        log.error("Model Already Exist exception thrown; {} ", ex.getMessage());

        return new AppResponse<>(HttpStatus.CONFLICT.value(), ex.getMessage(),
                ex.getMessage(), null, ex.getMessage());
    }

    @ExceptionHandler(ValidatorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppResponse<Object> handleValidatorException(final ValidatorException ex) {
        log.error("Validation error: {} ", ex.getMessage());
        return new AppResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
                ex.getMessage(), null, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public AppResponse<Object> handleNotFoundException(final NotFoundException ex) {
        log.error("Not found error: {} ", ex.getMessage());
        return new AppResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                ex.getMessage(), null, ex.getMessage());
    }

    @ExceptionHandler(PageableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppResponse<Object> handlePageableException(final PageableException ex) {
        log.error("Pageable error: {} ", ex.getMessage());
        return new AppResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
                ex.getMessage(), null, ex.getMessage());
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppResponse<Object> handleServerWebInputException(final ServerWebInputException ex) {
        log.error("Bad request error: {} ", ex.getMessage());
        return new AppResponse<>(HttpStatus.BAD_REQUEST.value(),
                messageSourceService.getMessageByKey("invalid.identifier"),
                messageSourceService.getMessageByKey("check.id"),
                null,
                messageSourceService.getMessageByKey("invalid.identifier"));
    }

    @ExceptionHandler({ TypeMismatchException.class, IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppResponse<Object> handleInvalidInputException(final Exception ex) {
        log.error("Bad request error: {} ", ex.getMessage());
        return new AppResponse<>(HttpStatus.BAD_REQUEST.value(),
                messageSourceService.getMessageByKey("invalid.identifier"),
                messageSourceService.getMessageByKey("check.id"),
                null,
                messageSourceService.getMessageByKey("invalid.identifier"));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<AppResponse<Object>> handleException(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String message = messageSourceService.getMessageByKey("validation.error");
        var response = new AppResponse<>(HttpStatus.BAD_REQUEST.value(), message, message, null, errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<AppResponse<Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        String message = messageSourceService.getMessageByKey("validation.error");
        var response = new AppResponse<>(HttpStatus.BAD_REQUEST.value(), message, message, null,
                e.getLocalizedMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<AppResponse<Object>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        String message = messageSourceService.getMessageByKey("header.missing.error");
        var response = new AppResponse<>(HttpStatus.BAD_REQUEST.value(), message, message, null, e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
