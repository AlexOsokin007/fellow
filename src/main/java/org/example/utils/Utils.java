package org.example.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.reporters.Files;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {

    private Utils() {
        // private constructor
    }

    public static void initWebDriverWaitPageLoad(WebDriver webDriver) {
        new WebDriverWait(webDriver, 100).until(driver -> executeScriptFromFile("src/main/resources/script/getReadyState.js", driver).equals("complete"));
    }

    public static Object executeScriptFromFile(String scriptFilePath, WebDriver webDriver, Object... args) {
        try (FileInputStream fis = new FileInputStream(scriptFilePath)) {
            String script = Files.readFile(fis);
            return ((JavascriptExecutor) webDriver).executeScript(script, args);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
