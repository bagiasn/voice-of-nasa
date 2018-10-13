package com.github.bagiasn.nasavoicecrawler.data.utils;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.github.bagiasn.nasavoicecrawler.ui.NlpEventsListener;

import java.util.ArrayList;

/**
 * This class handles events  received from {@link SpeechRecognizer} by implementing {@link RecognitionListener}.
 *
 * Results are pushed to another listener (the calling activity that is).
 *
 */
public class NlpListener implements RecognitionListener {
    // Class name for logging.
    private final String TAG = getClass().getSimpleName();

    private NlpEventsListener callback;

    public NlpListener(NlpEventsListener callback) {
        this.callback = callback;
    }

    public void onReadyForSpeech(Bundle params) {
        callback.onRecognizerReady();
    }

    public void onBeginningOfSpeech() { Log.d(TAG, "BOS"); }

    public void onRmsChanged(float rmsdB) { }

    public void onBufferReceived(byte[] buffer) { }

    public void onEndOfSpeech() { Log.d(TAG, "EOS"); }

    public void onError(int error) {
        // Adjust error output as much as it makes sense.
        switch (error) {
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                Log.e(TAG,"Insufficient permissions");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Log.e(TAG,"RecognitionService busy.");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                Log.e(TAG, "Network operation timed out.");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                Log.e(TAG, "Network related error");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                Log.e(TAG, "Recognition server sent error status.");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Log.e(TAG, "No speech input");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                Log.e(TAG, "No recognition result matched.");
                break;
            case SpeechRecognizer.ERROR_AUDIO:
                Log.e(TAG,"Audio recording error.");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                Log.e(TAG,"Client side error.");
                break;
            default:
                Log.e(TAG,"Unknown error.");
                break;
        }
        callback.onNlpError();
    }

    public void onResults(Bundle results) {
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (data == null || data.size() == 0) {
            callback.onNlpError();
            return;
        }
        Log.d(TAG, data.get(0));
        callback.onNlpResult(data.get(0));
    }

    public void onPartialResults(Bundle partialResults) {
    }

    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, String.valueOf(eventType));
    }
}
