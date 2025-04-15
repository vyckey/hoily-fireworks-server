package com.hoily.service.fireworks.infrastructure.common.validation;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Checker
 *
 * @author vyckey
 * @see CheckResult
 */
@FunctionalInterface
public interface Checker<T> extends Predicate<T> {
    CheckResult check(T target);

    @Override
    default boolean test(T target) {
        return check(target).isSuccess();
    }

    default Checker<T> andCheck(Checker<? super T> other) {
        Objects.requireNonNull(other);
        return (T target) -> {
            CheckResult result = check(target);
            if (result.isSuccess()) {
                return other.check(target).merge(result, true);
            }
            return result;
        };
    }

    default Checker<T> andCheck(Predicate<T> predicate, String failMessage, Object... args) {
        return andCheck(withFailMsg(predicate, failMessage, args));
    }

    default Checker<T> orCheck(Checker<? super T> other) {
        Objects.requireNonNull(other);
        return (T target) -> {
            CheckResult result = check(target);
            if (!result.isSuccess()) {
                return other.check(target).merge(result, true);
            }
            return result;
        };
    }

    default Checker<T> orCheck(Predicate<T> predicate, String successMessage, Object... args) {
        return orCheck(withSucMsg(predicate, successMessage, args));
    }

    default Checker<T> negCheck(String message) {
        return (T target) -> {
            CheckResult result = check(target);
            return result.update(!result.isSuccess(), message);
        };
    }

    static <T> Checker<T> success() {
        return t -> CheckResult.success();
    }

    static <T> Checker<T> success(String message, Object... args) {
        return of(true, message, args);
    }

    static <T> Checker<T> fail(String message, Object... args) {
        return of(false, message, args);
    }

    static <T> Checker<T> of(boolean success, String message, Object... args) {
        return target -> CheckResult.of(success, message, args);
    }

    static <T> Checker<T> of(Map.Entry<Boolean, String> resultPair) {
        return target -> CheckResult.of(resultPair);
    }

    static <T> Checker<T> withFailMsg(Predicate<T> predicate, String failMessage, Object... args) {
        return target -> predicate.test(target) ? CheckResult.success() : CheckResult.fail(failMessage, args);
    }

    static <T> Checker<T> withSucMsg(Predicate<T> predicate, String successMessage, Object... args) {
        return target -> predicate.test(target) ? CheckResult.success(successMessage, args) : CheckResult.fail();
    }
}