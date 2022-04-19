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
    public static final int MSG_TYPE_RECEIVED_STICKER = 2;
    public static final int MSG_TYPE_SENT_STICKER = 3;
    public static final int MSG_TYPE_RECEIVED_PHOTO = 4;
    public static final int MSG_TYPE_SENT_PHOTO = 5;
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
        if (viewType == MSG_TYPE_SENT_STICKER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_sent_message_sticker, parent, false);
            MessageSendStickerViewHolder sendStickerViewHolder = new MessageSendStickerViewHolder(view);
            return sendStickerViewHolder;
        }
        if (viewType == MSG_TYPE_SENT_PHOTO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_sent_message_photo, parent, false);
            MessageSendPhotoViewHolder sendPhotoViewHolder = new MessageSendPhotoViewHolder(view);
            return sendPhotoViewHolder;
        }
        if (viewType == MSG_TYPE_RECEIVED_STICKER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_received_message_sticker, parent, false);
            MessageReceiveStickerViewHolder receiveImgViewHolder = new MessageReceiveStickerViewHolder(view);
            return receiveImgViewHolder;
        }
        if (viewType == MSG_TYPE_RECEIVED_PHOTO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrainer_received_message_photo, parent, false);
            MessageReceivePhotoViewHolder receivePhotoViewHolder = new MessageReceivePhotoViewHolder(view);
            return receivePhotoViewHolder;
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
            mSentVH.tvDateTimeSent.setText(message.convertSecondsToHMm());
            mSentVH.tvMessageSent.setText(message.getMessage());
            mSentVH.itemSent.setOnClickListener(v -> iClickItemDetailMessage.onClickItemDetailMessage());
            mSentVH.tvMessageSent.setOnClickListener(v -> {
                mSentVH.tvMessageSent.setBackgroundResource(R.drawable.background_sent_message_single);
                mSentVH.tvDateTimeSent.setVisibility(View.VISIBLE);
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
            mReceiveVH.tvDateTimeReceived.setText(message.convertSecondsToHMm());
            mReceiveVH.tvMessageReceived.setText(message.getMessage());
            mReceiveVH.itemRecived.setOnClickListener(v -> iClickItemDetailMessage.onClickItemDetailMessage());
            mReceiveVH.tvMessageReceived.setOnClickListener(v -> {
                mReceiveVH.tvMessageReceived.setBackgroundResource(R.drawable.background_received_message_single);
                mReceiveVH.tvMessageReceived.setVisibility(View.VISIBLE);
            });

        } else if (holder.getItemViewType() == MSG_TYPE_SENT_STICKER) {

            MessageSendStickerViewHolder sendImgViewHolder = (MessageSendStickerViewHolder) holder;
            sendImgViewHolder.imgMessageSentSticker.setImageResource(Integer.parseInt(message.getMessage().trim()));
            sendImgViewHolder.tvDateTimeSentSticker.setText(message.convertSecondsToHMm());
            sendImgViewHolder.itemSentSticker.setOnClickListener(v -> iClickItemDetailMessage.onClickItemDetailMessage());

        } else if (holder.getItemViewType() == MSG_TYPE_SENT_PHOTO) {

            MessageSendPhotoViewHolder sendPhotoViewHolder = (MessageSendPhotoViewHolder) holder;
            if (!message.getMessage().equals("")) {
                Glide.with(context).load(message.getMessage().trim()).into(sendPhotoViewHolder.imgMessageSentPhoto);
            }
            sendPhotoViewHolder.tvDateTimeSent.setText(message.convertSecondsToHMm());
            sendPhotoViewHolder.itemSentPhoto.setOnClickListener(v -> iClickItemDetailMessage.onClickItemDetailMessage());
        } else if (holder.getItemViewType() == MSG_TYPE_RECEIVED_STICKER) {

            MessageReceiveStickerViewHolder receiveImgViewHolder = (MessageReceiveStickerViewHolder) holder;
            if (!imgUrl.equals("")) {
                Glide.with(context).load(imgUrl).into(receiveImgViewHolder.imgAvatarReceivedSticker);
            }
            receiveImgViewHolder.imgMessageReceivedSticker.setImageResource(Integer.parseInt(message.getMessage().trim()));
            receiveImgViewHolder.tvDateTimeReceivedSticker.setText(message.convertSecondsToHMm());
            receiveImgViewHolder.itemRecivedSticker.setOnClickListener(v -> iClickItemDetailMessage.onClickItemDetailMessage());
        } else if (holder.getItemViewType() == MSG_TYPE_RECEIVED_PHOTO) {

            MessageReceivePhotoViewHolder receivePhotoViewHolder = (MessageReceivePhotoViewHolder) holder;
            if (!imgUrl.equals("")) {
                Glide.with(context).load(imgUrl).into(receivePhotoViewHolder.imgAvatarReceivedPhoto);
            }
            if (!message.getMessage().equals("")) {
                Glide.with(context).load(message.getMessage().trim()).into(receivePhotoViewHolder.imgMessageReceivedPhoto);
            }
            receivePhotoViewHolder.tvDateTimeReceivedPhoto.setText(message.convertSecondsToHMm());
            receivePhotoViewHolder.itemRecivedPhoto.setOnClickListener(v -> iClickItemDetailMessage.onClickItemDetailMessage());
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
                return MSG_TYPE_SENT_STICKER;
            } else if (messagesList.get(position).getType().equals("img")) {
                return MSG_TYPE_SENT_PHOTO;
            } else return MSG_TYPE_SENT;

        } else {
            if (messagesList.get(position).getType().equals("emoji")) {
                return MSG_TYPE_RECEIVED_STICKER;
            } else if (messagesList.get(position).getType().equals("img")) {
                return MSG_TYPE_RECEIVED_PHOTO;
            } else return MSG_TYPE_RECEIVED;
        }
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

class MessageSendStickerViewHolder extends RecyclerView.ViewHolder {
    ImageView imgMessageSentSticker;
    TextView tvDateTimeSentSticker;
    ConstraintLayout itemSentSticker;

    public MessageSendStickerViewHolder(@NonNull View itemView) {
        super(itemView);
        imgMessageSentSticker = itemView.findViewById(R.id.imgMessageSentSticker);
        tvDateTimeSentSticker = itemView.findViewById(R.id.tvDateTimeSentSticker);
        itemSentSticker = itemView.findViewById(R.id.itemSentSticker);
    }
}

class MessageSendPhotoViewHolder extends RecyclerView.ViewHolder {
    ImageView imgMessageSentPhoto;
    TextView tvDateTimeSent;
    ConstraintLayout itemSentPhoto;

    public MessageSendPhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        imgMessageSentPhoto = itemView.findViewById(R.id.imgMessageSentPhoto);
        tvDateTimeSent = itemView.findViewById(R.id.tvDateTimeSentPhoto);
        itemSentPhoto = itemView.findViewById(R.id.itemSentPhoto);
    }
}

class MessageReceiveStickerViewHolder extends RecyclerView.ViewHolder {
    ImageView imgAvatarReceivedSticker;
    ImageView imgMessageReceivedSticker;
    TextView tvDateTimeReceivedSticker;
    ConstraintLayout itemRecivedSticker;

    public MessageReceiveStickerViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatarReceivedSticker = itemView.findViewById(R.id.imgAvatarReceivedSticker);
        imgMessageReceivedSticker = itemView.findViewById(R.id.imgMessageReceived);
        tvDateTimeReceivedSticker = itemView.findViewById(R.id.tvDateTimeReceivedSticker);
        itemRecivedSticker = itemView.findViewById(R.id.itemRecivedSticker);
    }
}

class MessageReceiveViewHolder extends RecyclerView.ViewHolder {
    ImageView imgAvatar;
    TextView tvMessageReceived;
    TextView tvDateTimeReceived;
    ConstraintLayout itemRecived;

    public MessageReceiveViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatar = itemView.findViewById(R.id.imgAvatarReceived);
        tvMessageReceived = itemView.findViewById(R.id.imgMessageReceived);
        tvDateTimeReceived = itemView.findViewById(R.id.tvDateTime);
        itemRecived = itemView.findViewById(R.id.itemRecived);
    }
}
class MessageReceivePhotoViewHolder extends RecyclerView.ViewHolder {
    ImageView imgAvatarReceivedPhoto;
    ImageView imgMessageReceivedPhoto;
    TextView tvDateTimeReceivedPhoto;
    ConstraintLayout itemRecivedPhoto;

    public MessageReceivePhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatarReceivedPhoto = itemView.findViewById(R.id.imgAvatarReceivedPhoto);
        imgMessageReceivedPhoto = itemView.findViewById(R.id.imgMessageReceivedPhoto);
        tvDateTimeReceivedPhoto = itemView.findViewById(R.id.tvDateTimeReceivedPhoto);
        itemRecivedPhoto = itemView.findViewById(R.id.itemRecivedPhoto);
    }
}

