package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rikkei.training.chat.Constants;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemDetailMessage;
import com.rikkei.training.chat.modle.Messages;

import java.util.List;

public class AdapterDetailMessage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MSG_TYPE_RECEIVED = 0;
    public static final int MSG_TYPE_SENT = 1;
    public static final int MSG_TYPE_RECEIVED_IMG = 2;
    public static final int MSG_TYPE_SENT_IMG = 3;
    FirebaseUser firebaseUser;
    List<Messages> messagesList;
    String imgUrl;
    Context context;
    IClickItemDetailMessage iClickItemDetailMessage;

    public AdapterDetailMessage(List<Messages> messagesList, String imgUrl, Context context, IClickItemDetailMessage iClickItemDetailMessage) {
        this.messagesList = messagesList;
        this.imgUrl = imgUrl;
        this.context = context;
        this.iClickItemDetailMessage = iClickItemDetailMessage;
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
        if (viewType == MSG_TYPE_SENT_IMG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_sent_message_img, parent, false);
            MessageSendImgViewHolder sendImgViewHolder = new MessageSendImgViewHolder(view);
            return sendImgViewHolder;
        }
        if (viewType == MSG_TYPE_RECEIVED_IMG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_received_message_img, parent, false);
            MessageReceiveImgViewHolder receiveImgViewHolder = new MessageReceiveImgViewHolder(view);
            return receiveImgViewHolder;
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
            mSentVH.itemSent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItemDetailMessage.onClickItemDetailMessage();
                }
            });
            mSentVH.tvMessageSent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSentVH.tvMessageSent.setBackgroundResource(R.drawable.background_sent_message_single);
                    mSentVH.tvDateTimeSent.setVisibility(View.VISIBLE);
                }
            });

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
            if (!imgUrl.equals("")) {
                Glide.with(context).load(imgUrl).into(mReceiveVH.imgAvatar);
            }
            mReceiveVH.tvDateTimeReceived.setText(Messages.convertSecondsToHMm(message.getTimeLong()));
            mReceiveVH.tvMessageReceived.setText(message.getMessage());
            mReceiveVH.itemRecived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItemDetailMessage.onClickItemDetailMessage();
                }
            });

        } else if (holder.getItemViewType() == MSG_TYPE_SENT_IMG) {
            MessageSendImgViewHolder sendImgViewHolder = (MessageSendImgViewHolder) holder;
            sendImgViewHolder.imgMessageSentImg.setImageResource(Integer.parseInt(message.getMessage().trim()));
            sendImgViewHolder.tvDateTimeSent.setText(Messages.convertSecondsToHMm(message.getTimeLong()));
        } else if(holder.getItemViewType() == MSG_TYPE_RECEIVED_IMG){
            MessageReceiveImgViewHolder receiveImgViewHolder = (MessageReceiveImgViewHolder) holder;
            if (!imgUrl.equals("")) {
                Glide.with(context).load(imgUrl).into(receiveImgViewHolder.imgAvatarReceivedImg);
            }
            receiveImgViewHolder.imgMessageReceived.setImageResource(Integer.parseInt(message.getMessage().trim()));
            receiveImgViewHolder.tvDateTimeReceivedImg.setText(Messages.convertSecondsToHMm(message.getTimeLong()));
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
            if (messagesList.get(position).getType().equals("emoji")) {
                return MSG_TYPE_SENT_IMG;
            } else {
                return MSG_TYPE_SENT;
            }

        } else {
            if (messagesList.get(position).getType().equals("emoji")) {
                return MSG_TYPE_RECEIVED_IMG;
            } else {
                return MSG_TYPE_RECEIVED;
            }
        }
    }
}

class MessageReceiveViewHolder extends RecyclerView.ViewHolder {
    ImageView imgAvatar;
    TextView tvMessageReceived;
    TextView tvDateTimeReceived;
    ConstraintLayout itemRecived;
    public MessageReceiveViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatar = itemView.findViewById(R.id.imgAvatarReceivedImg);
        tvMessageReceived = itemView.findViewById(R.id.imgMessageReceived);
        tvDateTimeReceived = itemView.findViewById(R.id.tvDateTimeReceivedImg);
        itemRecived = itemView.findViewById(R.id.itemRecived);
    }
}

class MessageSendViewHolder extends RecyclerView.ViewHolder {
    TextView tvMessageSent;
    TextView tvDateTimeSent;
    ConstraintLayout itemSent;
    public MessageSendViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessageSent = itemView.findViewById(R.id.textMessageSent);
        tvDateTimeSent = itemView.findViewById(R.id.tvDateTimeSent);
        itemSent = itemView.findViewById(R.id.itemSent);
    }
}

class MessageSendImgViewHolder extends RecyclerView.ViewHolder {
    ImageView imgMessageSentImg;
    TextView tvDateTimeSent;

    public MessageSendImgViewHolder(@NonNull View itemView) {
        super(itemView);
        imgMessageSentImg = itemView.findViewById(R.id.imgMessageSentImg);
        tvDateTimeSent = itemView.findViewById(R.id.tvDateTimeSentImg);
    }
}

class MessageReceiveImgViewHolder extends RecyclerView.ViewHolder {
    ImageView imgAvatarReceivedImg;
    ImageView imgMessageReceived;
    TextView tvDateTimeReceivedImg;

    public MessageReceiveImgViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatarReceivedImg = itemView.findViewById(R.id.imgAvatarReceivedImg);
        imgMessageReceived = itemView.findViewById(R.id.imgMessageReceived);
        tvDateTimeReceivedImg = itemView.findViewById(R.id.tvDateTimeReceivedImg);
    }
}
