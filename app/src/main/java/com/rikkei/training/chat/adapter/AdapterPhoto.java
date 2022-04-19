package com.rikkei.training.chat.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rikkei.training.chat.IClickItemPhotoListener;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemEmojiListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterPhoto extends RecyclerView.Adapter<PhotoViewHolder> {
    List<Uri> lstUri = new ArrayList<>();
    IClickItemPhotoListener iClickItemPhotoListener;

    public AdapterPhoto(List<Uri> lstUri,IClickItemPhotoListener iClickItemPhotoListener) {
        this.lstUri = lstUri;
        this.iClickItemPhotoListener = iClickItemPhotoListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_photo, parent, false);
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(view);
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = lstUri.get(position);
        holder.imgPhoto.setImageURI(uri);
        holder.itemPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemPhotoListener.onClickItemPhoto(uri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstUri.size();
    }
}

class PhotoViewHolder extends RecyclerView.ViewHolder {
    ImageView imgPhoto;
    ConstraintLayout itemPhoto;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        imgPhoto = itemView.findViewById(R.id.imgPhoto);
        itemPhoto = itemView.findViewById(R.id.itemPhoto);
    }
}