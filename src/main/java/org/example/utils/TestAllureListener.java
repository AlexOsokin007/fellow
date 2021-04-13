package org.example.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestAllureListener extends TestListenerAdapter {

    private static String getTestMethodName(ITestResult result) {
        return result.getMethod().getConstructorOrMethod().getName();
    }

    private static String getTestClassName(ITestResult result) {
        return result.getTestClass().getName();
    }

    @Attachment(value = "Скриншот страницы", type = "image/png")
    public byte[] captureScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}", type = "text/txt")
    public static String saveTextLog(String message) {
        return message;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object webDriverAttribute = result.getTestContext().getAttribute(getTestClassName(result) + getTestMethodName(result));
        captureScreenshot((WebDriver) webDriverAttribute);
        saveTextLog("Тест '" + getTestMethodName(result) + "' упал. Сделан скриншот.");
    }
}
