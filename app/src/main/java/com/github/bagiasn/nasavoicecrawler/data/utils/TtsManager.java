package com.github.bagiasn.nasavoicecrawler.data.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

public class TtsManager {
    private TextToSpeech ttsInstance;
    private String lastText;
    private TtsEventListener callback;

    public TtsManager(Context context, TtsEventListener callback) {
        this.callback = callback;
        this.ttsInstance = new TextToSpeech(context, status -> {
            if (TextToSpeech.ERROR != status) {
                ttsInstance.setLanguage(Locale.forLanguageTag("el_GR"));
                ttsInstance.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {

                    }

                    @Override
                    public void onDone(String utteranceId) {
                        if (lastText.equals(utteranceId)) {
                            callback.onDone();
                        }
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });
            }
        });
    }

    public void speak(String text) {
        if (ttsInstance != null) {
            lastText = String.valueOf(text.hashCode());
            ttsInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, lastText);
        }
    }

    public void stop() {
        if (ttsInstance != null && ttsInstance.isSpeaking()) {
            ttsInstance.stop();
        }
    }

    public void terminate() {
        if (ttsInstance != null) {
            ttsInstance.shutdown();
        }
    }
}
