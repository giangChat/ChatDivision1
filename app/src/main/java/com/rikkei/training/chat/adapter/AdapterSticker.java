package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemEmojiListener;
import java.util.List;

public class AdapterSticker extends RecyclerView.Adapter<StickerViewHolder> {
    Context context;
    List<Integer> lstEmoji;
    IClickItemEmojiListener iClickItemEmojiListener;

    public AdapterSticker(Context context, List<Integer> lstEmoji, IClickItemEmojiListener iClickItemEmojiListener) {
        this.context = context;
        this.lstEmoji = lstEmoji;
        this.iClickItemEmojiListener = iClickItemEmojiListener;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_sticker, parent, false);
        StickerViewHolder stickerViewHolder = new StickerViewHolder(view);
        return stickerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Integer index = lstEmoji.get(position);
        holder.imgEmoji.setImageResource(index);
        holder.itemEmoji.setOnClickListener(v -> iClickItemEmojiListener.onClickItemEmoji(index));

    }

    @Override
    public int getItemCount() {
        return lstEmoji.size();
    }
}

class StickerViewHolder extends RecyclerView.ViewHolder {
    ImageView imgEmoji;
    ConstraintLayout itemEmoji;

    public StickerViewHolder(@NonNull View itemView) {
        super(itemView);
        imgEmoji = itemView.findViewById(R.id.imgSticker);
        itemEmoji = itemView.findViewById(R.id.itemSticker);
    }
}
