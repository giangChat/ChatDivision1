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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rikkei.training.chat.Constants;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.modle.Messages;

import java.util.List;

public class AdapterDetailMessage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MSG_TYPE_RECEIVED = 0;
    public static final int MSG_TYPE_SENT = 1;
    FirebaseUser firebaseUser;

    List<Messages> messagesList;
    String imgUrl;
    Context context;

    public AdapterDetailMessage(List<Messages> messagesList, String imgUrl, Context context) {
        this.messagesList = messagesList;
        this.imgUrl = imgUrl;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_sent_message, parent, false);
            MessageSendViewHolder messageSendViewHolder = new MessageSendViewHolder(view);
            return messageSendViewHolder;
        }
        if (viewType == MSG_TYPE_RECEIVED) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_received_message, parent, false);
            MessageReceiveViewHolder messageReceiveViewHolder = new MessageReceiveViewHolder(view);
            return messageReceiveViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message = messagesList.get(position);

        if (holder.getItemViewType() == MSG_TYPE_SENT) {

            MessageSendViewHolder mSentVH = (MessageSendViewHolder) holder;
            if (message.getStatus() == Constants.SINGLE) {
                mSentVH.tvMessageSent.setBackgroundResource(R.drawable.background_sent_message_single);
            } else if (message.getStatus() == Constants.START) {
                mSentVH.tvMessageSent.setBackgroundResource(R.drawable.background_sent_message_start);
                mSentVH.tvDateTimeSent.setVisibility(View.GONE);
            } else if (message.getStatus() == Constants.CENTER) {
                mSentVH.tvMessageSent.setBackgroundResource(R.drawable.background_sent_message_center);
                mSentVH.tvDateTimeSent.setVisibility(View.GONE);
            } else if (message.getStatus() == Constants.END) {
                mSentVH.tvMessageSent.setBackgroundResource(R.drawable.background_sent_message_end);
            }
            mSentVH.tvDateTimeSent.setText(Messages.convertSecondsToHMm(message.getTimeLong()));
            mSentVH.tvMessageSent.setText(message.getMessage());

        } else if (holder.getItemViewType() == MSG_TYPE_RECEIVED) {
            MessageReceiveViewHolder mReceiveVH = (MessageReceiveViewHolder) holder;
            if (message.getStatus() == Constants.SINGLE) {
                mReceiveVH.tvMessageReceived.setBackgroundResource(R.drawable.background_received_message_single);
            } else if (message.getStatus() == Constants.START) {
                mReceiveVH.tvMessageReceived.setBackgroundResource(R.drawable.background_received_message_start);
                mReceiveVH.tvDateTimeReceived.setVisibility(View.GONE);
                mReceiveVH.imgAvatar.setVisibility(View.INVISIBLE);
            } else if (message.getStatus() == Constants.CENTER) {
                mReceiveVH.tvMessageReceived.setBackgroundResource(R.drawable.background_received_message_center);
                mReceiveVH.tvDateTimeReceived.setVisibility(View.GONE);
                mReceiveVH.imgAvatar.setVisibility(View.INVISIBLE);
            } else if (message.getStatus() == Constants.END) {
                mReceiveVH.tvMessageReceived.setBackgroundResource(R.drawable.background_received_message_end);
            }
            if(!imgUrl.equals("default")) {
                Glide.with(context).load(imgUrl).into(mReceiveVH.imgAvatar);
            }
            mReceiveVH.tvMessageReceived.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messagesList.get(position).getIdSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_SENT;
        } else {
            return MSG_TYPE_RECEIVED;
        }
    }
}

class MessageReceiveViewHolder extends RecyclerView.ViewHolder {
    ImageView imgAvatar;
    TextView tvMessageReceived;
    TextView tvDateTimeReceived;

    public MessageReceiveViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatar = itemView.findViewById(R.id.imgAvatarReceived);
        tvMessageReceived = itemView.findViewById(R.id.textMessageReceived);
        tvDateTimeReceived = itemView.findViewById(R.id.tvDateTimeReceived);
    }
}

class MessageSendViewHolder extends RecyclerView.ViewHolder {
    TextView tvMessageSent;
    TextView tvDateTimeSent;

    public MessageSendViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessageSent = itemView.findViewById(R.id.textMessageSent);
        tvDateTimeSent = itemView.findViewById(R.id.tvDateTimeSent);
    }
}
