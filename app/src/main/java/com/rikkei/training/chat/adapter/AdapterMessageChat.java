package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.modle.Conversation;
import com.rikkei.training.chat.modle.Messages;
import com.rikkei.training.chat.modle.User;

import java.util.List;

public class AdapterMessageChat extends RecyclerView.Adapter<AdapterMessageChat.MessageViewHolder> {
    List<Conversation> conversationList;
    Context context;

    public AdapterMessageChat(List<Conversation> userList, Context context) {
        this.conversationList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        MessageViewHolder viewHolder = new MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);
        if(!conversation.getImgPhoto().equals("default")) {
            Glide.with(context).load(conversation.getImgPhoto()).into(holder.imageUserMessage);
        }
        holder.tvlastTime.setText(Messages.convertSecondsToHMm(conversation.getLastTime()));
        holder.tvNameUserMessage.setText(conversation.getFullName());
        holder.tvLastMessage.setText(conversation.getFullName());

    }

    @Override
    public int getItemCount() {
        if (conversationList == null)
            return 0;
        else
            return conversationList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUserMessage;
        TextView tvNameUserMessage;
        TextView tvLastMessage;
        TextView tvlastTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUserMessage = itemView.findViewById(R.id.imageUserMessage);
            tvNameUserMessage = itemView.findViewById(R.id.tvLastMessage);
            tvlastTime = itemView.findViewById(R.id.tvlastTime);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);

        }
    }
}
