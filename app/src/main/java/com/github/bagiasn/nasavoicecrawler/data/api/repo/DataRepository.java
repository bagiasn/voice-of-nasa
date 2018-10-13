package com.github.bagiasn.nasavoicecrawler.data.api.repo;

import android.util.Log;

import com.github.bagiasn.nasavoicecrawler.data.api.ApiClient;
import com.github.bagiasn.nasavoicecrawler.data.api.AppExecutor;
import com.github.bagiasn.nasavoicecrawler.data.api.nlu.ApiResponse;
import com.github.bagiasn.nasavoicecrawler.data.api.nlu.NluApi;
import com.github.bagiasn.nasavoicecrawler.data.api.nlu.NluRequestBody;
import com.github.bagiasn.nasavoicecrawler.data.helper.Constants;
import com.github.bagiasn.nasavoicecrawler.ui.MainActivity;
import com.github.bagiasn.nasavoicecrawler.ui.NlpEventsListener;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 *  Repository class for decoupling data-related operations from UI.
 */
public class DataRepository {
    private static final String TAG = DataRepository.class.getSimpleName();

    private static DataRepository instance;

    private AppExecutor executor;
    private NluApi nluApi;

    private DataRepository() {
        executor = new AppExecutor();
        nluApi = ApiClient.getClient().getRetrofit().create(NluApi.class);
    }

    public synchronized static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    /**
     * Issue an API request and pass the results on the listener.
     */
    public void performNluRequest(String text, int state, NlpEventsListener callback) {
        executor.getBackgroundIO().execute(() -> {
            // Create the request body.
            NluRequestBody body = new NluRequestBody();
            body.setText(text);
            body.setLanguage(Constants.APP_LANGUAGE);
            body.setState(state);

            Call<ApiResponse> call = nluApi.getNluResult(body);
            try {
                Response<ApiResponse> response = call.execute();
                if (response != null && response.isSuccessful()) {
                    Log.i(TAG, "Result successful");
                    ApiResponse result = response.body();
                    if (result != null && !result.hasError()) {
                        MainActivity.setState(result.getState());
                        ArrayList<String> info = new ArrayList<>(3);
                        info.add(result.getTitle());
                        info.add(result.getUriPath());
                        info.add(result.getTextToSpeak());
                        callback.onLoadResult(result.getType(), info);
                    }
                } else {
                    Log.e(TAG, "API response is unsuccessful :(");
                }
            } catch (IOException e) {
                Log.e(TAG, "API call failed: " + e.getMessage());
            }
        });
    }

}
