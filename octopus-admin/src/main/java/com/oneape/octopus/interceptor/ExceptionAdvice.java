package com.oneape.octopus.interceptor;

import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.common.UnauthorizedException;
import com.oneape.octopus.model.VO.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("Missing request parameters:{}", e.getMessage(), e);
        return ApiResult.ofError(StateCode.BadRequest, "Missing request parameters: " + e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Parameter resolution failed:{}", e.getMessage(), e);
        return ApiResult.ofError(StateCode.BadRequest);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Parameter validation fails:{}", e.getMessage(), e);
        String message = getErrorMessage(e.getBindingResult());
        return ApiResult.ofError(StateCode.BadRequest, message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResult<String> handleBindException(BindException e) {
        log.error("Parameter binding failed:{}", e.getMessage(), e);
        String message = getErrorMessage(e.getBindingResult());
        return ApiResult.ofError(StateCode.BadRequest, message);
    }

    private String getErrorMessage(BindingResult result) {
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();

        return String.format("%s:%s", field, code);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult<String> handleServiceException(ConstraintViolationException e) {
        log.error("Parameter validation fails:{}", e.getMessage(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return ApiResult.ofError(StateCode.BadRequest, message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiResult<String> handleValidationException(ValidationException e) {
        log.error("Parameter validation fails:{}", e.getMessage(), e);
        return ApiResult.ofError(StateCode.BadRequest, "Parameter validation fails");
    }

    /**
     * 401 - unauthorized
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ApiResult<String> handleUnauthorizedException(UnauthorizedException e) {
        log.error("Unauthorized operation");
        return ApiResult.ofError(StateCode.Unauthorized);
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("The current request method is not supported:{}", e.getMessage());
        return ApiResult.ofError(StateCode.MethodNotAllowed);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ApiResult<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        String errorMsg = e.getMessage();
        return ApiResult.ofError(StateCode.BizError, errorMsg);
    }
}
