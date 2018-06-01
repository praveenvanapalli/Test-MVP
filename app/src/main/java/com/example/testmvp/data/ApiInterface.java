package com.example.testmvp.data;

import com.example.testmvp.model.GetDataReponse;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by Praveen
 */

public interface ApiInterface {

    @GET("svc/mostpopular/v2/mostviewed/all-sections/7.json?apikey=72a1f5f1b2bc43dcab344a4f9b4f3f13")
    Call<GetDataReponse> getNews();
}
