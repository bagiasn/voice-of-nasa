package com.github.bagiasn.nasavoicecrawler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.bagiasn.nasavoicecrawler.R;
import com.github.bagiasn.nasavoicecrawler.data.api.repo.DataRepository;
import com.github.bagiasn.nasavoicecrawler.data.helper.Constants;
import com.github.bagiasn.nasavoicecrawler.data.utils.NlpListener;
import com.wang.avi.AVLoadingIndicatorView;

public class MainActivity extends AppCompatActivity implements NlpEventsListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static boolean isRecognitionDisabled = false;

    private SpeechRecognizer speechRecognizer;
    private Intent recognitionIntent;
    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUi();

        setupUtil();
    }

    private void setupUtil() {
        // Setup the intent here for reusability.
        recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Constants.APP_LANGUAGE);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Constants.APP_LANGUAGE);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        // Make sure speech recognizer is available.
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.e(TAG, "SpeechRecognizer not available");
            isRecognitionDisabled = true;
        }

        dataRepository = DataRepository.getInstance();
    }

    private void setupUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (!isRecognitionDisabled) { startListening(); }
        });
    }

    private void startListening() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new NlpListener(this));
        speechRecognizer.startListening(recognitionIntent);
    }

    @Override
    public void onNlpResult(String result) {
        dataRepository.performNluRequest(result, this);

        closeRecognizer();
    }

    @Override
    public void onNlpError() {
        // Show error popup.

        // Clean speechRecognizer.
        closeRecognizer();
    }

    @Override
    public void onRecognizerReady() {
        AVLoadingIndicatorView indicatorView = findViewById(R.id.main_listeningIndicator);
        indicatorView.post(indicatorView::show);
    }

    @Override
    public void onLoadImage(String imageUrl) {
        runOnUiThread(() -> {
//            ImageView imgHolder = findViewById(R.id.main_image_holder);
//            Glide.with(MainActivity.this)
//                    .load(imageUrl)
//                    .into(imgHolder);
        });
    }

    private void closeRecognizer() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        AVLoadingIndicatorView indicatorView = findViewById(R.id.main_listeningIndicator);
        indicatorView.post(indicatorView::smoothToHide);
    }
}
