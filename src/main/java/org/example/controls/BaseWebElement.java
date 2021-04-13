package org.example.controls;

import org.awaitility.Awaitility;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Базовый класс для методов веб-элемента
 */
public abstract class BaseWebElement {
    protected final Logger log = getLogger(getClass());
    protected WebElement element;
    protected List<WebElement> elements;
    protected WebDriver webDriver;
    protected WebDriverWait wait;

    protected BaseWebElement(WebElement element, WebDriver webDriver) {
        this.element = element;
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, 60);
    }

    protected BaseWebElement(WebElement element, WebDriver webDriver, long timeOutInSeconds) {
        this.element = element;
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, timeOutInSeconds);
    }

    protected BaseWebElement(List<WebElement> elements, WebDriver webDriver) {
        this.elements = elements;
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, 60);
    }

    protected BaseWebElement(List<WebElement> elements, WebDriver webDriver, long timeOutInSeconds) {
        this.elements = elements;
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, timeOutInSeconds);
    }

    public WebElement getElement() {
        return element;
    }

    /**
     * Ожидаем видимость веб-элемента
     *
     * @param element - веб-элемент
     * @return - веб-элемент
     */
    public WebElement waitForElement(WebElement element) {
        log.info("Ожидаем видимость веб-элемента {}", element);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Ожидаем кликабельность веб-элемента
     *
     * @param element - веб-элемент
     * @return - веб-элемент
     */
    public WebElement waitForClickableElement(WebElement element) {
        log.info("Ожидаем кликабельность веб-элемента");
        waitForElement(element);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Проверка прогрузки страницы, опираясь на веб-элемент
     *
     * @param text - текст, содержащийся в элементе
     */
    public void checkChangePage(String text) {
        log.info("Проверка прогрузки страницы, опираясь на веб-элемент {}", element);
        waitForElement(element);
        Awaitility.given().pollDelay(300, TimeUnit.MILLISECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .and().timeout(10, TimeUnit.SECONDS)
                .await("Ключевой элемент не найден")
                .until(() -> element.getText().contains(text));
    }

    /**
     * Получаем текст аттрибута элемента
     *
     * @param attribute - название аттрибута
     * @return - текст аттрибута
     */
    public String getAttributeText(String attribute) {
        log.info("Получаем текст из аттрибута элемента {}", element);
        waitForElement(element);
        return element.getAttribute(attribute);
    }

    /**
     * Кликаем по элементу
     *
     * @return - объект BaseWebElement
     */
    public BaseWebElement click() {
        log.info("Кликаем по элементу {}", element);
        waitForClickableElement(element);
        element.click();
        return this;
    }

    public void scrollToElement(WebElement element) {
        int elementPosition = element.getLocation().getY();
        String js = String.format("window.scroll(0, %s)", elementPosition);
        ((JavascriptExecutor) webDriver).executeScript(js);
    }

    public String getCurrentUrl(){
        return webDriver.getCurrentUrl();
    }
}
