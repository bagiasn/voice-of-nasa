package com.github.bagiasn.nasavoicecrawler.ui;

import java.util.ArrayList;

/**
 * Interface for handling data operations results on the UI level.
 */
public interface NlpEventsListener {

    void onNlpResult(String result);

    void onNlpError();

    void onRecognizerReady();

    void onLoadResult(int type, ArrayList<String> result);
}
