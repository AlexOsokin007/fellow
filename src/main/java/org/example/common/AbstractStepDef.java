package org.example.common;

import io.restassured.http.Cookies;
import org.example.utils.ResponseValueExtractable;

import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;

public abstract class AbstractStepDef implements ResponseValueExtractable {

    private Cookies sessionCookies;
    private Map<String, String> headers = emptyMap();
    private final String jsonScheme;
    protected String baseUrl;

    protected AbstractStepDef(Platforms platform) {
        this.sessionCookies = new Cookies();
        jsonScheme = format("%s/%s.json", platform.toString(), "%s");
    }

    public String getJsonScheme(String schemeName) {
        return format(jsonScheme, schemeName);
    }

    public Cookies getCookies() {
        return sessionCookies;
    }

    protected void setCookies(Cookies cookies) {
        sessionCookies = cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    protected void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
