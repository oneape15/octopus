package com.oneape.octopus.interceptor;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.StateCode;
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
        log.error("缺少请求参数:{}", e.getMessage(), e);
        return new ApiResult<>(StateCode.BadRequest, "缺少请求参数" + e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败:{}", e.getMessage(), e);
        return new ApiResult<>(StateCode.BadRequest, "参数解析失败");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数验证失败:{}", e.getMessage(), e);
        String message = getErrorMessage(e.getBindingResult());
        return new ApiResult<>(StateCode.BadRequest, message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResult<String> handleBindException(BindException e) {
        log.error("参数绑定失败:{}", e.getMessage(), e);
        String message = getErrorMessage(e.getBindingResult());
        return new ApiResult<>(StateCode.BadRequest, message);
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
        log.error("参数验证失败:{}", e.getMessage(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return new ApiResult<>(StateCode.BadRequest, message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiResult<String> handleValidationException(ValidationException e) {
        log.error("参数验证失败:{}", e.getMessage(), e);
        return new ApiResult<>(StateCode.BadRequest, "参数验证失败");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("不支持当前请求方法:{}", e.getMessage());
        return new ApiResult<>(StateCode.MethodNotAllowed);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ApiResult<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        String errorMsg = e.getMessage();
        // 错误消息为空 或者消息长度超过一定长度的认定为未捕获到的异常
        if (StringUtils.isBlank(errorMsg) || StringUtils.length(errorMsg) > 50) {
            errorMsg = StateCode.InternalServerError.getMessage();
        }
        return new ApiResult<>(errorMsg);
    }
}
