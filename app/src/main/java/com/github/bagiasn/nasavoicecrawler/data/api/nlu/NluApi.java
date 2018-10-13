package com.github.bagiasn.nasavoicecrawler.data.api.nlu;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NluApi {
    @POST("/voiceSearch")
    Call<ApiResponse> getNluResult(@Body NluRequestBody body);
}
