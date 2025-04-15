package com.hoily.service.fireworks.contract.response;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Base response
 *
 * @author vyckey
 */
@Getter
@ToString
public class BaseResponse<T> implements Serializable {
    protected boolean success;
    protected Integer code;
    protected String message;
    protected T result;

    private BaseResponse() {
    }

    private BaseResponse(Builder<T> builder) {
        this.success = builder.success;
        this.code = builder.code;
        this.message = builder.message;
        this.result = builder.result;
    }

    public Builder<T> toBuilder() {
        return new Builder<T>(success).code(code).message(message).result(result);
    }

    public static <T> BaseResponse<T> successResponse() {
        return new Builder<T>(true).build();
    }

    public static <T> Builder<T> success() {
        return new Builder<>(true);
    }

    public static <T> BaseResponse<T> successResponse(T result) {
        return success(result).build();
    }

    public static <T> Builder<T> success(T result) {
        return new Builder<T>(true).result(result);
    }

    public static <T> BaseResponse<T> failResponse(int errorCode, String errorMsg) {
        return new Builder<T>(false).code(errorCode).message(errorMsg).build();
    }

    public static <T> Builder<T> fail() {
        return new Builder<>(false);
    }

    public static <T> Builder<T> fail(int errorCode, String errorMsg) {
        return new Builder<T>(false).code(errorCode).message(errorMsg);
    }

    public static final class Builder<T> {
        private final boolean success;
        private Integer code;
        private String message;
        private T result;

        private Builder(boolean success) {
            this.success = success;
        }

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> result(T result) {
            this.result = result;
            return this;
        }

        public BaseResponse<T> build() {
            return new BaseResponse<>(this);
        }
    }

}

