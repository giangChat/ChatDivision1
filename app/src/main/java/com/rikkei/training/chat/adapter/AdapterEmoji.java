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

import java.util.ArrayList;
import java.util.List;

public class AdapterEmoji extends RecyclerView.Adapter<ImgEmojiViewHolder> {
    Context context;
    List<Integer> lstEmoji = new ArrayList<>();
    IClickItemEmojiListener iClickItemEmojiListener;

    public AdapterEmoji(Context context, List<Integer> lstEmoji, IClickItemEmojiListener iClickItemEmojiListener) {
        this.context = context;
        this.lstEmoji = lstEmoji;
        this.iClickItemEmojiListener = iClickItemEmojiListener;
    }

    @NonNull
    @Override
    public ImgEmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_emoji, parent, false);
        ImgEmojiViewHolder imgEmojiViewHolder = new ImgEmojiViewHolder(view);
        return imgEmojiViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImgEmojiViewHolder holder, int position) {
        Integer index = lstEmoji.get(position);
        holder.imgEmoji.setImageResource(index);
        holder.itemEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemEmojiListener.onClickItemEmoji(index);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstEmoji.size();
    }
}

class ImgEmojiViewHolder extends RecyclerView.ViewHolder {
    ImageView imgEmoji;
    ConstraintLayout itemEmoji;

    public ImgEmojiViewHolder(@NonNull View itemView) {
        super(itemView);
        imgEmoji = itemView.findViewById(R.id.imgEmojiItem);
        itemEmoji = itemView.findViewById(R.id.itemEmoji);
    }
}
