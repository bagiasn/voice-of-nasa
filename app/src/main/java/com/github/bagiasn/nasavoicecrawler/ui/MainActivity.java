package com.github.bagiasn.nasavoicecrawler.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.bagiasn.nasavoicecrawler.R;
import com.github.bagiasn.nasavoicecrawler.data.api.repo.DataRepository;
import com.github.bagiasn.nasavoicecrawler.data.helper.Constants;
import com.github.bagiasn.nasavoicecrawler.data.utils.NlpListener;
import com.github.bagiasn.nasavoicecrawler.data.utils.TtsEventListener;
import com.github.bagiasn.nasavoicecrawler.data.utils.TtsManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NlpEventsListener, TtsEventListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static boolean isRecognitionDisabled = false;

    private SpeechRecognizer speechRecognizer;
    private Intent recognitionIntent;
    private DataRepository dataRepository;

    private TextSwitcher startTextSwitcher;
    private TextSwitcher textSwitcher;
    private TtsManager ttsManager;

    private static int mainState = 1;

    public static void setState(int state) {
        mainState = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUi();

        setupUtil();
    }

    @Override
    protected void onDestroy() {
        if (ttsManager != null) ttsManager.terminate();

        super.onDestroy();
    }

    @Override
    public void onNlpResult(String result) {
        dataRepository.performNluRequest(result, mainState,false,this);

        closeRecognizer();
    }

    @Override
    public void onNlpError() {
        // Clean speechRecognizer.
        closeRecognizer();
        // Send empty request to keep things going.
        dataRepository.performNluRequest("", mainState,false,this);
    }

    @Override
    public void onRecognizerReady() {
        AVLoadingIndicatorView indicatorView = findViewById(R.id.main_listeningIndicator);
        indicatorView.post(indicatorView::show);
    }

    @Override
    public void onLoadResult(int type, ArrayList<String> result) {
        if (type == 0) {
            runOnUiThread(() -> changeText(result.get(2)));
            ttsManager.speak(result.get(2));
        } else {
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putStringArrayListExtra(Constants.EXTRA_INFO_LIST, result);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (mainState == 10) return;
                dataRepository.performNluRequest("", mainState, true,this);
            }
        }
    }

    @Override
    public void onDone() {
        if (mainState < 10) {
            startListening();
        } else {
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.post(fab::show);
        }
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

        ttsManager = new TtsManager(getApplicationContext(), this);

        dataRepository = DataRepository.getInstance();
    }

    private void setupUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.astronaut_48);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (!isRecognitionDisabled) {
                ttsManager.stop();
                // Listen!
                startListening();
            }
        });

        setupTextSwitchers();

        new Handler().postDelayed(() -> {
            startTextSwitcher.setText(getString(R.string.main_description));
            ttsManager.speak(getString(R.string.main_description));
        }, 1500);
    }

    private void startListening() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            runOnUiThread(this::startSpeechRecognizer);
        } else {
            startSpeechRecognizer();
        }
    }

    private void startSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechRecognizer.setRecognitionListener(new NlpListener(MainActivity.this));
        speechRecognizer.startListening(recognitionIntent);
    }

    private void closeRecognizer() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        AVLoadingIndicatorView indicatorView = findViewById(R.id.main_listeningIndicator);
        indicatorView.post(indicatorView::smoothToHide);
    }

    private void setupTextSwitchers() {
        startTextSwitcher = findViewById(R.id.main_start_text_switcher);
        startTextSwitcher.setFactory(startViewFactory);
        textSwitcher = findViewById(R.id.main_text_switcher);
        textSwitcher.setFactory(mainViewFactory);
        // Set the in/out animations. Using the defaults.
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        in.setDuration(300);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        out.setDuration(300);
        textSwitcher.setInAnimation(in);
        startTextSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);
        startTextSwitcher.setOutAnimation(out);
    }

    private ViewSwitcher.ViewFactory startViewFactory = () -> {
        TextView textView = new TextView(MainActivity.this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        return textView;
    };

    private ViewSwitcher.ViewFactory mainViewFactory = () -> {
        TextView textView = new TextView(MainActivity.this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setLineSpacing(1.2f, 1);
        return textView;
    };

    /**
     * Hacky, dirty method for using different text switchers for different occasions.
     *
     * All this just to be able to use different text sizes.
     *
     * But it's a hackathon so don't worry.
     *
     * @param newText the updated text to set.
     */
    private void changeText(String newText) {
        if (newText.length() > 200) {
            startTextSwitcher.setCurrentText("");
            textSwitcher.setText(newText);
        } else {
            textSwitcher.setCurrentText("");
            startTextSwitcher.setText(newText);
        }
    }
}
