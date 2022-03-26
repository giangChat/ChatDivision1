package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.modle.StatusFriends;
import com.rikkei.training.chat.modle.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllFriends extends RecyclerView.Adapter<AdapterAllFriends.ViewHolder> {
    List<User> userList;
    List<StatusFriends> statusFriendsList;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser userCurrent;

    public AdapterAllFriends(List<User> userList, List<StatusFriends> statusFriendsList, Context context) {
        this.userList = userList;
        this.statusFriendsList = statusFriendsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterAllFriends.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort_name, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllFriends.ViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null)
            return;
        holder.tvUserName.setText(user.getFullName());
        holder.butConfirm.setVisibility(View.GONE);
        if (!user.getImgUrl().trim().equals("default")) {
            Glide.with(context).load(user.getImgUrl()).into(holder.imgUser);
        }
        String name = getName(user.getFullName()).charAt(0) + "";
        if (position == 0) {
            holder.tvNameFirst.setText(name);
            holder.tvNameFirst.setVisibility(View.VISIBLE);
        } else {
            User userFirst = userList.get(position - 1);
            String nameFirst = getName(userFirst.getFullName()).charAt(0) + "";
            if (nameFirst.equals(name)) {
                holder.tvNameFirst.setVisibility(View.GONE);
            } else {
                holder.tvNameFirst.setText(name);
                holder.tvNameFirst.setVisibility(View.VISIBLE);
            }
        }
        holder.butConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                userCurrent = FirebaseAuth.getInstance().getCurrentUser();
                StatusFriends statusFriends = new StatusFriends(user.getId(), "invite friend", "default");
                databaseReference.child("friend").child(userCurrent.getUid()).child(user.getId()).setValue(statusFriends);
                StatusFriends statusFriends1 = new StatusFriends(userCurrent.getUid(), "agree", "default");
                databaseReference.child("friend").child(user.getId()).child(userCurrent.getUid()).setValue(statusFriends1);
                holder.butConfirm.setVisibility(View.GONE);
            }
        });
        for (StatusFriends s : statusFriendsList) {
            if (s.getId().equals(user.getId())) {
                holder.butConfirm.setVisibility(View.GONE);
                break;
            }
        }
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

    public String getName(String fullName) {
        String[] tg = fullName.split(" ");
        return tg[tg.length - 1];
    }
}
