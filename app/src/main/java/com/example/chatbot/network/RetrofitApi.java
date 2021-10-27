package com.example.chatbot.network;

import com.example.chatbot.model.MsgModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitApi {
    @GET
    Call<MsgModel> getMessage(@Url String url);
}
