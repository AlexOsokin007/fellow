package org.example.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import org.example.utils.JsonHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;

import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import static io.restassured.config.DecoderConfig.decoderConfig;
import static java.lang.String.format;
import static org.example.utils.APIUtils.getPath;
import static org.example.utils.APIUtils.loadDataAsString;

public abstract class BaseTest<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
    private static final String JSON_PATH = "json/request/%s/";
    private static final Properties allureProp = new Properties();
    private final String reqJsonPath;
    private T data;
    private WebDriver webDriver;

    static {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config().redirect(RedirectConfig.redirectConfig().followRedirects(false)).decoderConfig(decoderConfig().noContentDecoders());
        try {
            allureProp.load(new FileInputStream(getPath("allure.properties").toString()));
        } catch (IOException e) {
            LOG.error("Непредвиденная ошибка при обработке allure.properties {}", e.getMessage());
        }
    }

    private BaseTest(String platform){
        reqJsonPath = format(JSON_PATH, platform);
    }

    protected BaseTest(Platforms platform) {
        this(platform.toString());
    }

    protected BaseTest(T data, Platforms platform) {
        this(platform.toString());
        Path standJsonPath = Paths.get(".").toAbsolutePath()
                .normalize().resolve("src/test/resources/settings/" + platform.toString() + "Settings.json").toAbsolutePath();
        try (Reader reader = new FileReader(standJsonPath.toString())) {
            Gson gson = new Gson();
            this.data = gson.fromJson(reader, (Type) data.getClass());
        } catch (FileNotFoundException e) {
            LOG.error("Файл {} не найден", standJsonPath, e);
        } catch (IOException e) {
            LOG.error("Ошибка разбора json {}", standJsonPath, e);
        }
    }

    /**
     * Инициализация веб-драйвера
     */
    protected void initWebDriver() throws MalformedURLException, URISyntaxException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=ru", "--ignore-certificate-errors",
                "--ignore-urlfetcher-cert-requests", "--start-maximized", "--disable-extensions");


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "91.0");
        capabilities.setCapability("moon:options", ImmutableMap.<String, Object>builder().put("enableVNC", true).build());

        options.merge(capabilities);

        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(
                URI.create("https://<>:<>@dvpr-api-moon-03.moon-ds5-genr03.corp.dev.vtb/wd/hub").toURL(),
                options
        );


        /*ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=ru", "--ignore-certificate-errors",
                "--ignore-urlfetcher-cert-requests", "--start-maximized", "--disable-extensions");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "92.0");
        capabilities.setCapability("selenoid:options", ImmutableMap.<String, Object>builder().put("enableVNC", true).build());
        options.merge(capabilities);

        RemoteWebDriver remoteWebDriver = null;

        remoteWebDriver = new RemoteWebDriver(
                URI.create("https://vtb4048869:Osokin531732!@dvpr-api-moon-03.moon-ds5-genr03.corp.dev.vtb/wd/hub").toURL(),
                options
        );
        //remoteWebDriver.setFileDetector(new LocalFileDetector());
        /*try {
            remoteWebDriver = new RemoteWebDriver(
                    URI.create("https://vtb4048869:%21@dvpr-api-moon-02.moon-ds5-genr03.corp.dev.vtb").toURL(),
                    options
            );
            remoteWebDriver.setFileDetector(new LocalFileDetector());
        } catch (Exception e) {
            LOG.error("Ошибка при инициализации драйвера {}", e.getMessage());
        }*/
        webDriver = remoteWebDriver;


        /*WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();*/
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    protected T getData() {
        return data;
    }

    protected void closeWebDriver() {
        webDriver.quit();
    }

    protected String getReqJson(String jsonName) {
        return loadDataAsString(format(reqJsonPath.concat("%s.json"), jsonName));
    }

    protected String getReqJson(String tmpJsonName, Map<String, ?> newValues) {
        String jsonTmp = getReqJson(tmpJsonName);
        return JsonHelper.setValuesInJson(jsonTmp, newValues);
    }

    protected String getJsonFileName(String jsonName) {
        return format(reqJsonPath.concat("%s"), jsonName);
    }

    @BeforeMethod(alwaysRun = true)
    public void initTestContext(ITestContext testContext, ITestResult testResult) {
        testContext.setAttribute(testResult.getTestClass().getName() + testResult.getMethod().getConstructorOrMethod().getName(), getWebDriver());
    }
}
