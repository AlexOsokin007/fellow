package org.example.pages.fellow;

import org.example.controls.*;
import org.example.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Описание главной страницы сайта Fellow
 * https://ifellow.ru/
 */
public class FellowMainPage extends BasePage {
    private WebDriver webDriver;

    public FellowMainPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    /**
     * Вкладка 'О нас'
     */
    @FindBy(xpath = "//a[contains(text(), 'О нас')]")
    private WebElement about;

    /**
     * Закрыть всплывающие окно
     */
    @FindBy(xpath = "//button[@aria-label='Закрыть']")
    private WebElement closeAlert;

    public Link getAboutLink() {
        return new Link(about, webDriver);
    }

    public Button getCloseAlertLink() {
        return new Button(closeAlert, webDriver);
    }

    /**
     * Переход на страницу 'О нас'
     */
    public FellowAboutPage goToAboutPage() {
        getCloseAlertLink().click();
        getAboutLink().click();
        return new FellowAboutPage(webDriver);
    }
}