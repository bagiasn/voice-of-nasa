package com.github.bagiasn.nasavoicecrawler.data.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TtsManager {
    private TextToSpeech ttsInstance;

    public TtsManager(Context context) {
        this.ttsInstance = new TextToSpeech(context, status -> {
            if (TextToSpeech.ERROR != status) {
                ttsInstance.setLanguage(Locale.forLanguageTag("el_GR"));
            }
        });
    }

    public void speak(String text) {
        if (ttsInstance != null) {
            ttsInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, String.valueOf(text.hashCode()));
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
