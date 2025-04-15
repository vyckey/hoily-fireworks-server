package com.hoily.service.fireworks.infrastructure.common.validation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Check result
 *
 * @author vyckey
 * @see Checker
 */
@Getter
@EqualsAndHashCode(of = {"success", "message"})
public class CheckResult implements Map.Entry<Boolean, String> {
    private boolean success;
    private String message;
    private final List<Record> records = new ArrayList<>();

    protected CheckResult(boolean success, String message) {
        update(success, message);
    }

    public CheckResult update(boolean success, String message) {
        this.success = success;
        this.message = message;
        records.add(new Record(success, message));
        return this;
    }

    public CheckResult addRecord(boolean success, String message) {
        records.add(new Record(success, message));
        return this;
    }

    public CheckResult merge(CheckResult result, boolean front) {
        if (front) {
            records.addAll(0, result.getRecords());
        } else {
            records.addAll(result.getRecords());
        }
        return this;
    }

    @Override
    public Boolean getKey() {
        return success;
    }

    @Override
    public String getValue() {
        return message;
    }

    @Override
    public String setValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(success);
        if (message != null) {
            sb.append("(").append(message).append(")");
        }
        return sb.toString();
    }

    public <T> CheckResult andCheck(T target, Checker<T> checker) {
        return success ? checker.check(target).merge(this, true) : this;
    }

    public <T> CheckResult andCheck(T target, Predicate<T> predicate, String failMessage, Object... args) {
        return success ? Checker.withFailMsg(predicate, failMessage, args).check(target).merge(this, true) : this;
    }

    public <T> CheckResult andCheckAll(Iterable<T> target, Checker<T> checker) {
        CheckResult result = this;
        Iterator<T> iterator = target.iterator();
        while (result.success && iterator.hasNext()) {
            result = checker.check(iterator.next());
        }
        return result.merge(this, true);
    }

    public <T> CheckResult andCheckAll(Iterable<T> target, Predicate<T> predicate, String failMessage, Object... args) {
        return andCheckAll(target, Checker.withFailMsg(predicate, failMessage, args));
    }

    private static String formatMessage(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }

    public static CheckResult of(boolean success, String message, Object... args) {
        return new CheckResult(success, formatMessage(message, args));
    }

    public static CheckResult of(boolean success, String successMessage, String failMessage) {
        String message = success ? successMessage : failMessage;
        return new CheckResult(success, message);
    }

    public static CheckResult of(Map.Entry<Boolean, String> pair) {
        return new CheckResult(pair.getKey(), pair.getValue());
    }

    public static CheckResult success() {
        return new CheckResult(true, null);
    }

    public static CheckResult success(String message, Object... args) {
        return new CheckResult(true, formatMessage(message, args));
    }

    public static CheckResult fail() {
        return new CheckResult(false, null);
    }

    public static CheckResult fail(String message, Object... args) {
        return new CheckResult(false, formatMessage(message, args));
    }

    /**
     * Checker check record
     */
    public static class Record implements Map.Entry<Boolean, String> {
        private final boolean success;
        private final String message;

        Record(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        @Override
        public Boolean getKey() {
            return success;
        }

        @Override
        public String getValue() {
            return message;
        }

        @Override
        public String setValue(String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return success + " -> " + message;
        }
    }
}