package org.example.controls;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Методы для веб-элемента Поле ввода
 */
public class Field extends BaseWebElement{

    public Field(WebElement element, WebDriver webDriver) {
        super(element, webDriver);
    }

    /**
     * Вводим текст в поле ввода
     *
     * @param var - текст для ввода
     * @return - объект Field
     */
    public Field setText(CharSequence... var) {
        log.info("Вводим тест в поле ввода {}", element);
        waitForElement(element);
        element.sendKeys(var);
        return this;
    }

    /**
     * Удаляем текст в поле ввода
     *
     * @return - объект Field
     */
    public Field deleteText() {
        log.info("Удаляем тест в поле ввода {}", element);
        waitForElement(element);
        element.clear();
        return this;
    }

    /**
     * Получаем текст в поле ввода
     *
     * @return - текст в поле ввода
     */
    public String getText() {
        log.info("Получаем текст в поле ввода {}", element);
        waitForElement(element);
        return element.getAttribute("value");
    }
}
