package com.github.bagiasn.nasavoicecrawler.data.api.nlu;

/**
 * This class represents the API response to {@link ApiRequest}.
 */
public class ApiResponse {
    private boolean error;
    private String title;
    private String photo;
    private String text;

    public boolean hasError() {
        return error;
    }

    public String getUriPath() {
        return photo;
    }

    public String getTextToSpeak() {
        return text;
    }

    public String getTitle() {
        return title;
    }

}
