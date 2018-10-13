package com.github.bagiasn.nasavoicecrawler.data.api.repo;

import android.util.Log;

import com.github.bagiasn.nasavoicecrawler.data.api.ApiClient;
import com.github.bagiasn.nasavoicecrawler.data.api.AppExecutor;
import com.github.bagiasn.nasavoicecrawler.data.api.nlu.ApiResponse;
import com.github.bagiasn.nasavoicecrawler.data.api.nlu.NluApi;
import com.github.bagiasn.nasavoicecrawler.data.api.nlu.NluRequestBody;
import com.github.bagiasn.nasavoicecrawler.data.helper.Constants;

import java.io.IOException;

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
     * Issue an API request and pass the results.
     */
    public void performNluRequest(String text) {
        executor.getBackgroundIO().execute(() -> {
            // Create the request body.
            NluRequestBody body = new NluRequestBody();
            body.setText(text);
            body.setLanguage(Constants.APP_LANGUAGE);

            Call<ApiResponse> call = nluApi.getNluResult(body);
            try {
                Response<ApiResponse> response = call.execute();
                if (response != null) {
                    Log.i(TAG, "Result: " + response.message());
                } else {
                    Log.e(TAG, "API response is null :(");
                }
            } catch (IOException e) {
                Log.e(TAG, "API call failed: " + e.getMessage());
            }
        });
    }

}
