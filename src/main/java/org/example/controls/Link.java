package org.example.controls;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Методы для веб-элемента Ссылка
 */
public class Link extends BaseWebElement {

    public Link(WebElement element, WebDriver webDriver){
        super(element, webDriver);
    }

    /**
     * Кликаем по элементу
     *
     * @return - объект Link
     */
    @Override
    public Link click() {
        log.info("Кликаем по элементу {}", element);
        waitForClickableElement(element);
        element.click();
        return this;
    }

    /**
     * Получаем ссылку из элемента
     *
     * @return - ссылка
     */
    public String getLink() {
        log.info("Получаем ссылку из элемента {}", element);
        waitForElement(element);
        return element.getAttribute("href");
    }

    /**
     * Получаем текст ссылки элемента
     *
     * @return - текст
     */
    public String getText() {
        log.info("Получаем текст ссылки элемента {}", element);
        waitForElement(element);
        return element.getText();
    }

    /**
     * Проверка активность элемента ссылки
     *
     * @return - true - активно
     */
    public boolean isEnabled() {
        log.info("Проверка активность элемента кнопки {}", element);
        waitForElement(element);
        return element.isEnabled();
    }
}
