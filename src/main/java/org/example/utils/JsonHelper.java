package org.example.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Утилитный класс для работы с json-файлами.
 */
public final class JsonHelper {
    private static final Logger LOG = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper objectMapper = setMapper();

    private JsonHelper() {
        throw new IllegalAccessError("Это утилитный класс. Создание экземпляра не требуется.");
    }

    private static ObjectMapper setMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    /**
     * Устанавливает значение в переданном json и возвращает измененный json в виде строки.
     *
     * @param json      json, в котором нужно установить значение
     * @param pathToKey путь до значения
     * @param newValue  новое значение
     * @return измененный json в виде строки
     */
    public static String setValueInJson(String json, String pathToKey, Object newValue) {
        return JsonPath.parse(json, Configuration.defaultConfiguration()).set(pathToKey, newValue).jsonString();
    }

    /**
     * Устанавливает значения в переданном json и возвращает измененный json в виде строки.
     *
     * @param json      json, в котором нужно установить новые значения
     * @param newValues map вида "путь до значения, новое значение"
     * @return измененный json в виде строки
     */
    public static String setValuesInJson(String json, Map<String, ?> newValues) {
        final DocumentContext context = JsonPath.parse(json, Configuration.defaultConfiguration());
        newValues.forEach(context::set);
        return new UnicodeUnescaper().translate(context.jsonString());
    }

    /**
     * Добавляет в переданный json новые поля и возвращает измененный json в виде строки.
     *
     * @param json      исходный json
     * @param path      путь, куда нужно вставить новые поля
     * @param newValues map с новыми полями вида "название поля, значение"
     * @return измененный json в виде строки
     */
    public static String putValuesInJson(String json, String path, Map<String, ?> newValues) {
        final DocumentContext context = JsonPath.parse(json, Configuration.defaultConfiguration());
        newValues.forEach((pathToKey, newValue) -> context.put(path, pathToKey, newValue));
        return context.jsonString();
    }

    /**
     * Формирует json из значения по jsonPath и возвращает его.
     *
     * @param json     входной json
     * @param jsonPath путь до ключа
     * @return новый json
     */
    public static Object getBodyAsJsonString(String json, String jsonPath) {
        return new JSONObject(JsonPath.parse(json).read(jsonPath)); // toJSONString()
    }

    /**
     * Преобразует {@link Map} в json-style {@link String}.
     *
     * @param json {@link Map}, которую нужно преобразовать в json-style строку
     * @return json-style {@link String}
     */
    public static String getJsonStringFromMap(Map<String, ?> json) {
        try {
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            LOG.error("Can't convert map {} to JSON-string", json);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Формирует json-строку из любого объекта и возвращает её.
     */
    public static String getDtoAsString(Object dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            LOG.info("Ошибка преобразования JSON", e);
            throw new RuntimeException("Не удалось преобразовать json в String");
        }
    }

    /**
     * Формирует объект типа T из json-строки и возвращает его.
     */
    public static <T> T readValue(String json, TypeReference<T> dtoType) {
        try {
            return objectMapper.readValue(json, dtoType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Формирует JsonNode из строки и возвращает её.
     * удобно использовать для сравнения двух json
     */
    public static JsonNode readTree(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
