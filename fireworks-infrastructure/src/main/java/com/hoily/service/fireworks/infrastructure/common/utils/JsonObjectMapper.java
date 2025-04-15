package com.hoily.service.fireworks.infrastructure.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * {@link ObjectMapper} for JSON
 *
 * <p>example 1:</p>
 * <pre>{@code
 * @Data
 * class Student {
 *     private Integer sAge;
 *     private String sName;
 * }
 *
 * Student student = JsonObjectMapper.CAMEL_CASE.fromJson("{\"sAge\": 18, \"sName\":\"Bob\"}", Student.class);
 * String json = JsonObjectMapper.SNAKE_CASE.toJson(student);
 * Assert.assertEquals("{\"s_age\":18,\"s_name\":\"Bob\"}", json);
 * }</pre>
 *
 * <p>example 2:</p>
 * <pre>{@code
 * class MyJsonUtils {
 *     public static final JsonObjectMapper INSTANCE = new JsonObjectMapper(new ObjectMapper()
 *             .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
 *             // custom features for yourself
 *     );
 * }
 * }</pre>
 *
 * @author vyckey
 */
public class JsonObjectMapper {
    private static final Logger log = LoggerFactory.getLogger(JsonObjectMapper.class);
    public static final JsonObjectMapper CAMEL_CASE = new JsonObjectMapper(new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    );
    public static final JsonObjectMapper SNAKE_CASE = new JsonObjectMapper(new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    );

    private final ObjectMapper objectMapper;

    public JsonObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected void handleException(IOException e) {
        log.error(e.getMessage(), e);
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T fromJson(String json, JavaType valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T fromJson(String json, Type type) {
        try {
            JavaType javaType = TypeFactory.defaultInstance().constructType(type);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T fromJson(byte[] json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T fromJson(byte[] json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

    public <T> T convert(Object object, Class<T> clazz) {
        return objectMapper.convertValue(object, clazz);
    }

    public <T> T convert(Object object, TypeReference<T> type) {
        return objectMapper.convertValue(object, type);
    }
}
