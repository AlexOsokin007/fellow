package org.example.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpHeaders.CONNECTION;
import static org.apache.http.protocol.HTTP.CONN_KEEP_ALIVE;
import static org.hamcrest.Matchers.equalTo;

/**
 * Вспомогательный класс
 */
public final class APIUtils {
    public static final String SCHEMA_PATH = "json/scheme/";

    private static final Logger LOG = LoggerFactory.getLogger(APIUtils.class);
    private static ResponseSpecification successResSpec;
    private static ResponseSpecification notSuccessResSpec;

    private APIUtils() {
        throw new IllegalAccessError("Utility class. Do not instantiate!");
    }

    public static ResponseSpecification createResponseSpecification(String path, Object expectedValue) {
        return new ResponseSpecBuilder().expectStatusCode(200).expectBody(path, equalTo(expectedValue)).build();
    }

    public static Optional<Path> getResourcePath(String fileName) {
        if (fileName.isEmpty()) {
            throw new IllegalArgumentException("Не задано имя файла!");
        }
        final Optional<Path> resourcePath;
        try {
            resourcePath = Optional.of(Paths.get(Objects.requireNonNull(
                    APIUtils.class.getClassLoader().getResource(fileName)).toURI()));
        } catch (URISyntaxException e) {
            Assert.fail("Не удалось найти файл: " + fileName, e);
            return Optional.empty();
        }
        return resourcePath;
    }

    public static String loadDataAsString(String path) {
        final Path json = getPath(path);
        return loadDataAsString(json);
    }

    public static Path getPath(String path) {
        return APIUtils.getResourcePath(path)
                .orElseThrow(() -> new IllegalArgumentException("Не найден путь до JSON"));
    }

    public static String loadDataAsString(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Can't find file: " + path.toString());
        }
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            LOG.error(e.getMessage());
            Allure.addAttachment("Response: ", e.toString());
            Assert.fail("Can't get data from: " + path);
            return null;
        }
    }

    public static void checkJsonScheme(Response response, String schemaPath) {
        response.then().body(matchesJsonSchemaInClasspath(SCHEMA_PATH + schemaPath));
    }

    public static Map<String, String> getHeaders(Map<String, String> headers) {
        final Map<String, String> headersMap = new HashMap<>();
        headersMap.put(CONNECTION, CONN_KEEP_ALIVE);
        if (headers == null) return headersMap;
        headersMap.putAll(headers);
        return headersMap;
    }

    public static Response sendGet(final String url, final Map<String, String> headers,
                                   final Map<String, ?> queryParams) {
        return sendGet(url, new Cookies(), headers, queryParams);
    }

    public static Response sendGet(final String url, final Cookies initCookies, final Map<String, String> headers,
                                   final Map<String, ?> queryParams) {
        return sendGet(url, initCookies, headers, queryParams, buildReqSpecification());
    }

    public static Response sendGet(final String url, final Cookies initCookies, final Map<String, String> headers,
                                   final Map<String, ?> queryParams, RequestSpecification requestSpecification) {
        final Map<String, ?> queryParameters = Optional.ofNullable(queryParams).orElseGet(HashMap::new);
        final Cookies cookies = Optional.ofNullable(initCookies).orElseGet(Cookies::new);
        final Map<String, String> headersMap = getHeaders(headers);

        return given()
                .spec(requestSpecification)
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .headers(headersMap)
                .queryParams(queryParameters)
                .cookies(cookies)
                .log()
                .all()
                .get(url)
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public static Response sendGet(final String url) {
        return sendGet(url, (Map<String, ?>) null);
    }

    public static Response sendGet(final String url, final String user, final String password) {
        return given()
                .filter(new AllureRestAssured())
                .auth()
                .preemptive()
                .basic(user, password)
                .log()
                .all()
                .get(url)
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public static Response sendGet(final String url, final Cookie... cookies) {
        return sendGet(url, new Cookies(cookies));
    }

    public static Response sendGet(final String url, final Cookies cookies) {
        return sendGet(url, cookies, null, null);
    }

    public static Response sendGet(final String url, final Map<String, ?> queryParams) {
        return sendGet(url, null, queryParams);
    }

    public static Response sendGetWithoutHeaders(final String url, final Cookies initCookies,
                                                 final Map<String, ?> queryParams) {
        return sendGet(url, initCookies, null, queryParams);
    }

    public static RequestSpecification buildReqSpecification() {
        return new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .build();
    }
}
