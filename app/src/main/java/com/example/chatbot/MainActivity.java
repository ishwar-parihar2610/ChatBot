package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatbot.adapter.ChatAdapter;
import com.example.chatbot.databinding.ActivityMainBinding;
import com.example.chatbot.model.ChatsModel;
import com.example.chatbot.model.MsgModel;
import com.example.chatbot.network.RetrofitApi;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModel> chatsModels;
    private ChatAdapter chatAdapter;
    private String msgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        chatsModels = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatsModels, MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatBotRecycleView.setLayoutManager(linearLayoutManager);
        binding.chatBotRecycleView.setAdapter(chatAdapter);
        binding.sendBtn.setOnClickListener(v -> {
            if (binding.msgEdit.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your Message", Toast.LENGTH_SHORT).show();
                return;
            }
            getResponse(binding.msgEdit.getText().toString());
            msgText=binding.msgEdit.getText().toString();
            binding.msgEdit.setText(null);

        });


    }

    private void getResponse(String message) {

        chatsModels.add(new ChatsModel(message, USER_KEY));
        chatAdapter.notifyDataSetChanged();
        String url = "get?bid=160826&key=GvhK3o0QB36UbJEZ&uid=[uid]&msg=" + message;

        String BASE_URl = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<MsgModel> call = retrofitApi.getMessage(url);
        call.enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if (response.isSuccessful()) {
                    MsgModel msgModel=response.body();
                    chatsModels.add(new ChatsModel(msgModel.getCnt(), BOT_KEY));
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                chatAdapter.notifyDataSetChanged();
                Log.d("MainActivity", "onFailure: " + t.getLocalizedMessage());
                getResponseAgain(msgText);


            }
        });

    }

    private void getResponseAgain(String message) {
        String url = "http://api.brainshop.ai/get?bid=160826&key=GvhK3o0QB36UbJEZ&uid=[uid]&msg=" + message;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String cnt = response.getString("cnt");
                    chatsModels.add(new ChatsModel(cnt, BOT_KEY));
                    chatAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                chatsModels.add(new ChatsModel("Please revert Your question", BOT_KEY));
                chatAdapter.notifyDataSetChanged();
            }

        });
        requestQueue.add(jsonObjectRequest);

    }
}

