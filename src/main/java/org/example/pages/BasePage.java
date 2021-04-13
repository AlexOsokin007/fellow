package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static org.example.utils.Utils.initWebDriverWaitPageLoad;

public class BasePage extends PageFactory {
    private final WebDriver webDriver;

    public BasePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        initWebDriverWaitPageLoad(webDriver);
        initElements(this.webDriver, this);
    }

    public void goToPage(String pageUrl) {
        if (webDriver != null) {
            webDriver.get(pageUrl);
        }
    }

    public void back() {
        if (webDriver != null) {
            webDriver.navigate().back();
        }
    }

    public void forward() {
        if (webDriver != null) {
            webDriver.navigate().forward();
        }
    }

    public void refresh() {
        if (webDriver != null) {
            webDriver.navigate().refresh();
        }
    }
}
