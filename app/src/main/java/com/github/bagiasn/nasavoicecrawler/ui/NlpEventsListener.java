package com.github.bagiasn.nasavoicecrawler.ui;

import com.github.bagiasn.nasavoicecrawler.data.api.nlu.ApiResponse;

/**
 * Interface for handling data operations results on the UI level.
 */
public interface NlpEventsListener {

    void onNlpResult(String result);

    void onNlpError();

    void onRecognizerReady();

    void onLoadResult(ApiResponse result);
}
