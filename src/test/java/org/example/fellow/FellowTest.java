package org.example.fellow;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.example.common.BaseTest;
import org.example.common.Platforms;
import org.example.utils.TestAllureListener;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@Listeners({TestAllureListener.class})
public class FellowTest extends BaseTest<FellowSettings> {

    private FellowStepDef fellowStepDef;

    protected FellowTest() {
        super(new FellowSettings(), Platforms.FELLOW);
    }

    @BeforeClass(alwaysRun = true)
    public void init() throws MalformedURLException, URISyntaxException {
        initWebDriver();
        fellowStepDef = new FellowStepDef(getWebDriver(), getData().getUrl());
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        closeWebDriver();
    }

    @Feature("Fellow")
    @Story("Проверить страницу 'О нас' и всех переходов по подразделам")
    @Test(description = "Проверить страницу 'О нас' и всех переходов по подразделам", groups = "ui")
    public void aboutUITest() {
        fellowStepDef.checkSubtasks(getData().getSubtasks());
    }

    @Feature("Fellow")
    @Story("Отфильтровать на странице 'Карьера' вакансии по фильтру 'Город' и проверить отфильтрованный список")
    @Test(description = "Отфильтровать на странице 'Карьера' вакансии по фильтру 'Город' и проверить отфильтрованный список", groups = "api")
    public void careerAPITest() {
        String limit = fellowStepDef.getLimit(getData().getUrlCareer(), getData().getQueryParams());
        getData().getQueryParams().put("limit", limit);
        fellowStepDef.checkCareerCity(getData().getUrlCareer(), getData().getQueryParams());
    }
}
