package org.example.fellow;

import java.util.List;
import java.util.Map;

public class FellowSettings {

    /**
     * Ссылка на Платформу Fellow
     */
    private String url;

    /**
     * Ссылка на страницу 'Карьера' Fellow
     */
    private String urlCareer;

    /**
     * Список подсистем
     */
    private List<String> subtasks;

    /**
     * Query параметры для запроса
     */
    private Map<String, String> queryParams;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<String> subtasks) {
        this.subtasks = subtasks;
    }

    public String getUrlCareer() {
        return urlCareer;
    }

    public void setUrlCareer(String urlCareer) {
        this.urlCareer = urlCareer;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }
}
