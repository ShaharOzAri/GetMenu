package com.example.getmenu.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.getmenu.MyApplication;
import com.example.getmenu.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExchangeRateModel {
    final public static ExchangeRateModel instance= new ExchangeRateModel();

    final String BASE_URL = "https://api.apilayer.com";
    Retrofit retrofit;
    ExchangeRateApi exchangeRateApi;
    private ExchangeRateModel(){
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        exchangeRateApi = retrofit.create(ExchangeRateApi.class);
    }

    public LiveData<Float> getExchangeRate(String from,String to){
        MutableLiveData<Float> data = new MutableLiveData<>();
        HashMap<String, String> map = new HashMap<String, String>();
        Call<ExchangeRate> call = exchangeRateApi.getExchangeRate(from, to, 1);
        call.enqueue(new Callback<ExchangeRate>() {
            @Override
            public void onResponse(Call<ExchangeRate> call, Response<ExchangeRate> response) {
                if(response.isSuccessful()){
                    ExchangeRate res = response.body();
                    data.setValue(res.getResult());
                }else{
                    Utils.print("failed to get data from exchange rate api");
                }
            }

            @Override
            public void onFailure(Call<ExchangeRate> call, Throwable t) {
                Utils.print("failed to get data from exchange rate api");
            }
        });

        return data;
    }
}
