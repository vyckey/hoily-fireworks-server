package com.hoily.jddd.core.repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Page Query
 *
 * @author vyckey
 */
@Getter
@EqualsAndHashCode
public class PageQuery {
    private final int offset;
    private final int pageNo;
    private final int pageSize;

    protected PageQuery(int pageNo, int pageSize) {
        this((pageNo - 1) * pageSize, pageNo, pageSize);
    }

    protected PageQuery(int offset, int pageNo, int pageSize) {
        if (offset < 0) {
            throw new IllegalArgumentException("invalid offset " + offset);
        }
        if (pageNo < 1) {
            throw new IllegalArgumentException("invalid pageNo " + pageNo);
        }
        if (pageSize < 0) {
            throw new IllegalArgumentException("invalid pageSize " + pageSize);
        }
        this.offset = offset;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public static PageQuery page(int pageNo, int pageSize) {
        return new PageQuery(pageNo, pageSize);
    }

    public static PageQuery page(int pageSize) {
        return new PageQuery(1, pageSize);
    }

    public static PageQuery from(int offset, int pageSize) {
        int pageNo = offset / pageSize + 1;
        return new PageQuery(offset, pageNo, pageSize);
    }

    @Override
    public String toString() {
        return "PageQuery{offset=" + offset + ", pageNo=" + pageNo + ", pageSize=" + pageSize + '}';
    }
}
