package com.hoily.jddd.core.repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Page result
 *
 * @author vyckey
 */
@Getter
@EqualsAndHashCode
public class PageResult<E> {
    private final long total;

    private final List<E> list;

    protected PageResult(long total, List<E> list) {
        if (total < 0L) {
            throw new IllegalArgumentException("invalid total " + total);
        }
        this.total = total;
        this.list = list != null ? list : new ArrayList<>();
    }

    public static <E> PageResult<E> empty(long total) {
        return new PageResult<>(total, null);
    }

    public static <E> PageResult<E> empty() {
        return new PageResult<>(0L, null);
    }

    public static <E> PageResult<E> create(long total, List<E> list) {
        return new PageResult<>(total, list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public <U> PageResult<U> map(Function<E, U> mapper) {
        Objects.requireNonNull(mapper);
        List<U> result = this.list.stream().map(mapper).collect(Collectors.toList());
        return new PageResult<>(this.total, result);
    }

    @Override
    public String toString() {
        return "PageResult{total=" + total + ", size=" + list.size() + '}';
    }
}

