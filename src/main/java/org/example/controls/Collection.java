package org.example.controls;

import org.awaitility.Awaitility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Методы для Списка веб-элементов
 */
public class Collection extends BaseWebElement {
    private final Actions actions;

    public Collection(List<WebElement> elements, WebDriver webDriver){
        super(elements, webDriver);
        actions = new Actions(this.webDriver);
    }

    /**
     * Кликаем по элементу с определенным номером в списке
     *
     * @param index - номер элемента в списке
     * @return - WebElement
     */
    public WebElement clickTo(int index) {
        log.info("Кликаем по элементу с определенным номером в списке");
        if (elements.isEmpty())
            throw new UnsupportedOperationException("Пустой список элементов");

        WebElement element = elements.get(index);
        waitForClickableElement(element);
        element.click();
        return element;
    }

    /**
     * Получаем текст из элемента
     *
     * @param index - номер элемента в списке
     * @return - текст из элемента
     */
    public String getText(int index) {
        log.info("Получаем текст из элемента");
        if (elements.isEmpty())
            throw new UnsupportedOperationException("Пустой список элементов");

        WebElement element = elements.get(index);
        return element.getText();
    }

    /**
     * Ожидаем количества элементов в списке больше 0
     *
     * @return - объект Collection
     */
    public Collection waitForElements() {
        log.info("Ожидаем количества элементов в списке больше 0");
        AtomicInteger size = new AtomicInteger();
        Awaitility.given().pollDelay(300, TimeUnit.MILLISECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .and().timeout(10, TimeUnit.SECONDS)
                .await("Количество элементов равно 0")
                .until(() -> {
                    boolean answer = !elements.isEmpty() && elements.size() == size.get();
                    size.set(elements.size());
                    return answer;
                });
        return this;
    }

    /**
     * Ожидаем отсутствие количества элементов
     *
     * @return - объект Collection
     */
    public Collection waitForEmpty() {
        log.info("Ожидаем отсутствие количества элементов");
        Awaitility.given().pollDelay(300, TimeUnit.MILLISECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .and().timeout(10, TimeUnit.SECONDS)
                .await("Количество элементов не равно 0")
                .until(() -> elements.isEmpty()
                );
        return this;
    }

    /**
     * Ожидаем определенного количества элементов
     *
     * @param size - ожидаемое количество элементов
     * @return - объект Collection
     */
    public Collection waitForElementsSize(int size) {
        log.info("Ожидаем количества элементов");
        Awaitility.given().pollDelay(300, TimeUnit.MILLISECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .and().timeout(10, TimeUnit.SECONDS)
                .await("Количество элементов не изменилось равно " + size)
                .until(() -> elements.size() == size);
        return this;
    }
}
