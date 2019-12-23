package com.example.invo.Parsing.Retrofit.Interface;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecyclerInterface {

    String JSONURL = "https://raw.githubusercontent.com/INVO2/server/master/";
    String JSON ="SAASD";

    @GET("pharmacy2.json")
    Call<String> getString();
}
