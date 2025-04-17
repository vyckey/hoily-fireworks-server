package com.hoily.service.fireworks.server.aop;

import com.hoily.service.fireworks.api.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * description is here
 *
 * @author vyckey
 */
@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class ApiControllerAdvice {
    private HttpServletRequest request;

    @ExceptionHandler({RuntimeException.class, Throwable.class, IOException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<?> handleAny(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return BaseResponse.failResponse(500000, "System Error");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    public BaseResponse<?> handleNotSupported(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage());
        return BaseResponse.failResponse(500000, "Bad media type");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleNotValid(MethodArgumentNotValidException e) {
        log.warn("request not valid {}", e.getMessage());
        String errorMsg = e.getBindingResult().hasErrors() ?
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage() : "invalid params";
        return BaseResponse.failResponse(500000, errorMsg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("request not readable, {}", e.getMessage(), e);
        return BaseResponse.failResponse(500000, "Bad request params or body");
    }

}
