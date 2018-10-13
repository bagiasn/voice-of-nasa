package com.github.bagiasn.nasavoicecrawler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.bagiasn.nasavoicecrawler.R;
import com.github.bagiasn.nasavoicecrawler.data.helper.Constants;
import com.github.bagiasn.nasavoicecrawler.data.utils.TtsEventListener;
import com.github.bagiasn.nasavoicecrawler.data.utils.TtsManager;

import java.util.List;

public class InfoActivity extends AppCompatActivity implements TtsEventListener {

    private TtsManager ttsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);

        Intent callingIntent = getIntent();
        if (callingIntent != null && callingIntent.hasExtra(Constants.EXTRA_INFO_LIST)) {
            List<String> info = callingIntent.getStringArrayListExtra(Constants.EXTRA_INFO_LIST);
            // Sanity checks.
            if (info == null) return;
            if (info.isEmpty() || info.size() != 3) return;

            // Set title.
            TextView tvTitle = findViewById(R.id.info_title);
            tvTitle.setText(info.get(0));
            // Load image.
            ImageView imgHolder = findViewById(R.id.info_image_holder);
            Glide.with(InfoActivity.this)
                    .load(Constants.API_SERVER + info.get(1))
                    .into(imgHolder);
            // Set TTS text.
            TextView tvTts = findViewById(R.id.info_tts);
            tvTts.setText(info.get(2));

            ttsManager = new TtsManager(getApplicationContext(), this);
            new Handler().postDelayed(() -> ttsManager.speak(info.get(2)), 2000);
        }
    }

    @Override
    protected void onDestroy() {
        if (ttsManager != null) ttsManager.terminate();

        super.onDestroy();
    }

    @Override
    public void onDone() {
        setResult(RESULT_OK);
        finish();
    }
}
