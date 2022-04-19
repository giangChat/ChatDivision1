package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemFriendListener;
import com.rikkei.training.chat.modle.Conversation;
import com.rikkei.training.chat.modle.Messages;
import com.rikkei.training.chat.modle.User;

import java.util.List;

public class AdapterMessageChat extends RecyclerView.Adapter<AdapterMessageChat.MessageViewHolder> {
    List<Conversation> conversationList;
    Context context;
    private IClickItemFriendListener iClickItemFriendListener;


    public AdapterMessageChat(List<Conversation> userList, Context context, IClickItemFriendListener iClickItemFriendListener) {
        this.conversationList = userList;
        this.context = context;
        this.iClickItemFriendListener = iClickItemFriendListener;
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
        if (!conversation.getImgPhoto().equals("default")) {
            Glide.with(context).load(conversation.getImgPhoto()).into(holder.imageUserMessage);
        }

        holder.tvlastTime.setText(Messages.setTextTimeSendMessage(conversation.getLastTime()));
        holder.tvNameUserMessage.setText(conversation.getFullName());
        if (conversation.getLastMessage().length() > 40 && conversation.getCoutUnSeen() >= 1) {
            String text = conversation.getLastMessage().substring(0, 40) + "...";
            holder.tvLastMessage.setText(text);
        } else {
            holder.tvLastMessage.setText(conversation.getLastMessage());
        }
        if (conversation.getCoutUnSeen() > 9 ) {
            holder.tvBadgeMessage.setText("9+");
            holder.tvBadgeMessage.setVisibility(View.VISIBLE);
            holder.imgBadgeBessage.setVisibility(View.VISIBLE);
            holder.tvLastMessage.setTextColor(Color.BLACK);

        } else if (conversation.getCoutUnSeen() >= 1) {
            holder.tvBadgeMessage.setText(String.valueOf(conversation.getCoutUnSeen()));
            holder.tvBadgeMessage.setVisibility(View.VISIBLE);
            holder.imgBadgeBessage.setVisibility(View.VISIBLE);
            holder.tvLastMessage.setTextColor(Color.BLACK);
            holder.tvlastTime.setTextColor(Color.BLACK);
        } else {
            holder.tvBadgeMessage.setText("");
            holder.tvBadgeMessage.setVisibility(View.INVISIBLE);
            holder.imgBadgeBessage.setVisibility(View.INVISIBLE);
            holder.tvLastMessage.setTextColor(Color.GRAY);
            holder.tvlastTime.setTextColor(Color.GRAY);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFriendListener.onClickItemFriend(conversation);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (conversationList == null)
            return 0;
        else
            return conversationList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        ImageView imageUserMessage;
        TextView tvBadgeMessage;
        TextView tvNameUserMessage;
        TextView tvLastMessage;
        TextView tvlastTime;
        ImageView imgBadgeBessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBadgeMessage = itemView.findViewById(R.id.tvBadgeMessage);
            constraintLayout = itemView.findViewById(R.id.itemMessage);
            imageUserMessage = itemView.findViewById(R.id.imageUserMessage);
            tvNameUserMessage = itemView.findViewById(R.id.tvNameUserMessage);
            tvlastTime = itemView.findViewById(R.id.tvlastTime);
            imgBadgeBessage = itemView.findViewById(R.id.imgBadgeBessage);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);

        }
    }
}
