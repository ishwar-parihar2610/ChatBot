package com.example.chatbot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot.R;

import com.example.chatbot.model.ChatsModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<ChatsModel> chatsModelArrayList;
    private Context context;

    public ChatAdapter(ArrayList<ChatsModel> chatsModelArrayList, Context context) {
        this.chatsModelArrayList = chatsModelArrayList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_recycle_view, parent,false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_recycle_view, parent,false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatsModel chatsModel = chatsModelArrayList.get(position);
        switch (chatsModel.getSender()) {
            case "user":
                ((UserViewHolder) holder).userTv.setText(chatsModel.getMessage());
                break;
            case "bot":
                ((BotViewHolder) holder).botTv.setText(chatsModel.getMessage());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatsModelArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chatsModelArrayList.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTv;

        public UserViewHolder(View itemView) {
            super(itemView);
            userTv = itemView.findViewById(R.id.userTv);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botTv;

        public BotViewHolder(View itemView) {
            super(itemView);
            botTv = itemView.findViewById(R.id.robotTextView);

        }
    }
}

