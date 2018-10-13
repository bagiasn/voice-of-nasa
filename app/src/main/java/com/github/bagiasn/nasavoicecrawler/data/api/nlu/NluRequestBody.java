package com.github.bagiasn.nasavoicecrawler.data.api.nlu;

public class NluRequestBody {
    private String text;
    private String language;
    private int state;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setState(int state) {
        this.state = state;
    }
}
