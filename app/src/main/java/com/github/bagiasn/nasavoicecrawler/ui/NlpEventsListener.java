package com.github.bagiasn.nasavoicecrawler.ui;

/**
 * Interface for handling data operations results on the UI level.
 */
public interface NlpEventsListener {

    void onNlpResult(String result);

    void onNlpError();

    void onRecognizerReady();
}
