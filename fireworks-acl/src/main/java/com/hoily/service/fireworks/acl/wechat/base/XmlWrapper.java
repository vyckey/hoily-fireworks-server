package com.hoily.service.fireworks.acl.wechat.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

/**
 * Xml object wrapper
 *
 * @author vyckey
 * 2023/2/14 13:51
 */
@JsonRootName("xml")
@Getter
public class XmlWrapper<T> {
    @JsonUnwrapped
    private final T object;

    @JsonCreator
    public XmlWrapper(T object) {
        this.object = object;
    }

    public static <T> XmlWrapper<T> of(T object) {
        return new XmlWrapper<>(object);
    }
}
