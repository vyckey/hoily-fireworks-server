package com.hoily.service.fireworks.infrastructure.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * description is here
 *
 * @author vyckey
 */
@Slf4j
public class XmlUtils {
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    private XmlUtils() {
    }

    public static String toXml(Object object) {
        try {
            return XML_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromXml(String xml, Class<T> valueType) {
        try {
            return XML_MAPPER.readValue(xml, valueType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromXml(String xml, TypeReference<T> valueTypeRef) {
        try {
            return XML_MAPPER.readValue(xml, valueTypeRef);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
