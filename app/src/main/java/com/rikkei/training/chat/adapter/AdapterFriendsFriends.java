package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.modle.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFriendsFriends extends RecyclerView.Adapter<AdapterFriendsFriends.ViewHolder> {
    List<User> userList;
    Context context;

    public AdapterFriendsFriends(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterFriendsFriends.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort_name, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFriendsFriends.ViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null)
            return;
        holder.tvUserName.setText(user.getFullName());

        holder.butConfirm.setVisibility(View.GONE);
        if (!user.getImgUrl().trim().equals("default")) {
            Glide.with(context).load(user.getImgUrl()).into(holder.imgUser);
        }
        String name=getName(user.getFullName()).charAt(0)+"";
        if(position==0){
            holder.tvNameFirst.setText(name);
        }else{
            User userFirst=userList.get(position-1);
            String nameFirst=getName(userFirst.getFullName()).charAt(0)+"";
            if(nameFirst.equals(name)){
                holder.tvNameFirst.setVisibility(View.GONE);
            }else{
                holder.tvNameFirst.setText(name);
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
            tvNameFirst=itemView.findViewById(R.id.tvNameFirst);
        }
    }

    public String getName(String fullName){
        String[] tg=fullName.split(" ");
        return tg[tg.length-1];
    }
}
