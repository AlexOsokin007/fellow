package org.example.fellow;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.example.common.AbstractStepDef;
import org.example.common.Platforms;
import org.example.pages.fellow.FellowAboutPage;
import org.example.pages.fellow.FellowMainPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyMap;
import static org.example.utils.APIUtils.*;

public class FellowStepDef extends AbstractStepDef {

    private FellowMainPage fellowMainPage;

    public FellowStepDef(WebDriver webDriver, String url){
        super(Platforms.FELLOW);
        fellowMainPage = new FellowMainPage(webDriver);
        fellowMainPage.goToPage(url);
    }

    @Step("Провека переходов по подразделам {}")
    public void checkSubtasks(List<String> subtasks) {
        FellowAboutPage fellowAboutPage = fellowMainPage.goToAboutPage();
        for (int i = 0; i < subtasks.size(); i++) {
            fellowAboutPage.getSubtasksCollection().waitForElementsSize(subtasks.size()).clickTo(i);
            String subtask = subtasks.get(i);
            Awaitility.given().pollDelay(300, TimeUnit.MILLISECONDS)
                    .pollInterval(1, TimeUnit.SECONDS)
                    .and().timeout(10, TimeUnit.SECONDS)
                    .await("Подсистемы не совпадают " + subtask)
                    .until(() -> subtask.contains("О компании") || subtask.contains("Руководство компании") ?
                                fellowAboutPage.getHeaderLabel().getText().contains(subtask) :
                                fellowAboutPage.getSecondHeaderLabel().getText().contains(subtask)
                    );
        }
    }

    @Step("Получаем колличество вакансий на {0}")
    public String getLimit(String url, Map<String, String> queryParams) {
        Response response = sendGet(url, emptyMap(), queryParams);
        response.then().assertThat().statusCode(200);
        return response.jsonPath().getString("DATA.total");
    }

    @Step("Проверяемпо схеме, что все вакансии соответствует городу Москва")
    public void checkCareerCity(String url, Map<String, String> queryParams) {
        Response response = sendGet(url, emptyMap(), queryParams);
        checkJsonScheme(response, getJsonScheme("fellowMoscowScheme"));
    }
}
