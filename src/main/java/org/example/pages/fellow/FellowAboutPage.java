package org.example.pages.fellow;

import org.example.controls.*;
import org.example.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Описание страницы 'О нас' сайта Fellow
 * https://ifellow.ru/about-us
 */
public class FellowAboutPage extends BasePage {
    private WebDriver webDriver;

    public FellowAboutPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    /**
     * Заголовок первого типа
     */
    @FindBy(xpath = "//div[@class='links']/ancestor::div[contains(@class, 'next')]//h1")
    private WebElement header;

    /**
     * Заголовок второго типа
     */
    @FindBy(xpath = "//div[@class='links']/ancestor::div[contains(@class, 'next')]//p[contains(@class, 'header')]")
    private WebElement secondHeader;

    /**
     * Подразделы
     */
    @FindBy(xpath = "//div[@class='links']/a")
    private List<WebElement> subtasks;

    public Label getHeaderLabel() {
        return new Label(header, webDriver);
    }

    public Label getSecondHeaderLabel() {
        return new Label(secondHeader, webDriver);
    }

    public Collection getSubtasksCollection() {
        return new Collection(subtasks, webDriver);
    }
}
