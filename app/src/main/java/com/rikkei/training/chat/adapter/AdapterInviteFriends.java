package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.modle.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterInviteFriends extends RecyclerView.Adapter<AdapterInviteFriends.ViewHolder> {
    List<User> userList;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser userCurrent;

    public AdapterInviteFriends(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterInviteFriends.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort_name, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInviteFriends.ViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null)
            return;
        if (!user.getImgUrl().trim().equals("default")) {
            Glide.with(context).load(user.getImgUrl()).into(holder.imgUser);
        }
        holder.tvUserName.setText(user.getFullName());
        holder.tvNameFirst.setVisibility(View.GONE);
        holder.butConfirm.setBackgroundResource(R.drawable.button_confirm_friend_type2);
        holder.butConfirm.setText("Hủy");
        holder.butConfirm.setTextColor(Color.BLACK);
        holder.butConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase=FirebaseDatabase.getInstance();
                databaseReference=firebaseDatabase.getReference();
                userCurrent= FirebaseAuth.getInstance().getCurrentUser();
                databaseReference.child("friend").child(userCurrent.getUid()).child(user.getId()).removeValue();
                databaseReference.child("friend").child(user.getId()).child(userCurrent.getUid()).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userList == null)
            return 0;
        else
            return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgUser;
        TextView tvUserName;
        AppCompatButton butConfirm;
        TextView tvNameFirst;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            butConfirm = itemView.findViewById(R.id.butConfirmFriends);
            tvNameFirst = itemView.findViewById(R.id.tvNameFirst);
        }
    }
}
