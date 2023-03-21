package com.example.getmenu.Model;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ExchangeRateApi {
    @Headers({"apikey:yPmDGgjWFFHU4oLLMkSThFaLzKcinbpl"})
    @GET("/exchangerates_data/convert")
    Call<ExchangeRate> getExchangeRate( @Query("from") String from, @Query("to") String to , @Query("amount") Integer amount);
}
