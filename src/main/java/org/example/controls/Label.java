package org.example.controls;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Методы для текстового веб-элемента
 */
public class Label extends BaseWebElement {
    public Label(WebElement element, WebDriver webDriver){
        super(element, webDriver);
    }

    /**
     * Получаем текст из текстового поля
     *
     * @return - текст
     */
    public String getText() {
        log.info("Получаем текст из текстового поля {}", element);
        waitForElement(element);
        return element.getText();
    }
}
