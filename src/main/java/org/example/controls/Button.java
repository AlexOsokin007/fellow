package org.example.controls;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Методы для веб-элемента Кнопка
 */
public class Button extends BaseWebElement {
    private final Actions actions;

    public Button(WebElement element, WebDriver webDriver){
        super(element, webDriver);
        actions = new Actions(this.webDriver);
    }

    /**
     * Кликаем по элементу
     *
     * @return - объект Button
     */
    @Override
    public Button click() {
        log.info("Кликаем по элементу {}", element);
        waitForClickableElement(element);
        element.click();
        return this;
    }

    /**
     * Дважды кликаем по элементу
     *
     * @return - объект Button
     */
    public Button dblClick() {
        log.info("Кликаем по элементу {}", element);
        waitForClickableElement(element);
        actions.doubleClick(element);
        return this;
    }

    /**
     * Проверка активность элемента кнопки
     *
     * @return - true - активно
     */
    public boolean isEnabled() {
        log.info("Проверка активность элемента кнопки {}", element);
        waitForElement(element);
        return element.isEnabled();
    }
}
