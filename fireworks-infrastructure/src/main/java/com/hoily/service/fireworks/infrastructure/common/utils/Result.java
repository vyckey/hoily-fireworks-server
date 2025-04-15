package com.hoily.service.fireworks.infrastructure.common.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Result with value and error
 *
 * @author vyckey
 */
public final class Result<T, E> {
    private final boolean ok;
    private final T value;
    private final E error;

    private Result(boolean ok, T value, E error) {
        this.ok = ok;
        this.value = value;
        this.error = error;
    }

    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(true, value, null);
    }

    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(false, null, error);
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isErr() {
        return !ok;
    }

    public Optional<T> ok() {
        return Optional.ofNullable(value);
    }

    public Optional<E> err() {
        return Optional.ofNullable(error);
    }

    public <U> Result<U, E> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper);
        if (isOk()) {
            return ok(mapper.apply(value));
        }
        return err(error);
    }

    public <F> Result<T, F> mapErr(Function<E, F> mapper) {
        Objects.requireNonNull(mapper);
        if (isOk()) {
            return ok(value);
        }
        return err(mapper.apply(error));
    }

    public <U> U mapOr(Function<T, U> mapper, U defaultValue) {
        Objects.requireNonNull(mapper);
        if (isOk()) {
            return mapper.apply(value);
        }
        return defaultValue;
    }

    public <U> U mapOrElse(Function<T, U> mapper, Function<E, U> errMapper) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(errMapper);
        if (isOk()) {
            return mapper.apply(value);
        }
        return errMapper.apply(error);
    }

    public Result<T, E> notNullOr(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        if (isOk() && value == null) {
            return ok(supplier.get());
        }
        return this;
    }

    public Result<T, E> inspect(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        if (isOk()) {
            consumer.accept(value);
        }
        return this;
    }

    public Result<T, E> inspectErr(Consumer<E> consumer) {
        Objects.requireNonNull(consumer);
        if (isErr()) {
            consumer.accept(error);
        }
        return this;
    }

    public T unwrap() {
        return unwrapOrThrow(() -> new IllegalStateException("not a ok result"));
    }

    public T expect(String message) {
        return unwrapOrThrow(() -> new IllegalStateException(message));
    }

    public T unwrapOr(T defaultValue) {
        if (isOk()) {
            return value;
        }
        return defaultValue;
    }

    public T unwrapOr(Supplier<T> defaultSupplier) {
        Objects.requireNonNull(defaultSupplier);
        if (isOk()) {
            return value;
        }
        return defaultSupplier.get();
    }

    public T unwrapOrElse(Function<E, T> errMapper) {
        Objects.requireNonNull(errMapper);
        if (isOk()) {
            return value;
        }
        return errMapper.apply(error);
    }

    public <X extends Throwable> T unwrapOrThrow(Supplier<X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier);
        if (isOk()) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    public <X extends Throwable> T unwrapOrThrow(Function<E, X> exceptionMapper) throws X {
        Objects.requireNonNull(exceptionMapper);
        if (isOk()) {
            return value;
        }
        throw exceptionMapper.apply(error);
    }

    public E unwrapErr() {
        return unwrapErrOrThrow(() -> new IllegalStateException("not a err result"));
    }

    public E expectErr(String message) {
        return unwrapErrOrThrow(() -> new IllegalStateException(message));
    }

    public <X extends Throwable> E unwrapErrOrThrow(Supplier<X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier);
        if (isOk()) {
            throw exceptionSupplier.get();
        }
        return error;
    }

    public <U> Result<U, E> and(Result<U, E> another) {
        if (isOk()) {
            return another;
        }
        return err(error);
    }

    public <U> Result<U, E> andThen(Function<T, Result<U, E>> mapper) {
        Objects.requireNonNull(mapper);
        if (isOk()) {
            return mapper.apply(value);
        }
        return err(error);
    }

    public <F> Result<T, F> or(Result<T, F> another) {
        if (isOk()) {
            return ok(value);
        }
        return another;
    }

    public <F> Result<T, F> orElse(Function<E, Result<T, F>> mapper) {
        Objects.requireNonNull(mapper);
        if (isOk()) {
            return ok(value);
        }
        return mapper.apply(error);
    }

    public Optional<Result<T, E>> transpose() {
        if (isOk() && value == null) {
            return Optional.empty();
        }
        return Optional.of(this);
    }

    public static <T, E> Result<T, E> flatten(Result<Result<T, E>, E> result) {
        return result.andThen(Function.identity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Result<?, ?> result = (Result<?, ?>) o;
        return ok == result.ok && Objects.equals(value, result.value) && Objects.equals(error, result.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ok, value, error);
    }

    @Override
    public String toString() {
        return (isOk() ? "Ok(" : "Err(") + value + ")";
    }

    @Override
    protected Result<T, E> clone() {
        return map(Function.identity());
    }
}
