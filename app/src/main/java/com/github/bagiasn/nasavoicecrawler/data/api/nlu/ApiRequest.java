package com.github.bagiasn.nasavoicecrawler.data.api.nlu;


import com.squareup.moshi.Json;

public class ApiRequest {
    @Json(name = "text")
    private String nlpResult;

    public String getNlpResult() {
        return nlpResult;
    }

    public void setNlpResult(String nlpResult) {
        this.nlpResult = nlpResult;
    }
}
