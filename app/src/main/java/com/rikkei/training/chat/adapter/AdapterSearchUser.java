package com.rikkei.training.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearchUser extends RecyclerView.Adapter<AdapterSearchUser.ViewHolder> implements Filterable {
    private List<User> userList, listSearch;
    private Context context;
    private TextView tvFriends;
    private TextView tvNoFindSearch;
    private ImageView imgNoFindSearch;
    private EditText edSearch;

    public AdapterSearchUser(List<User> userList, Context context, TextView tvFriends, TextView tvNoFindSearch, ImageView imgNoFindSearch, EditText edSearch) {
        this.userList = userList;
        this.context = context;
        this.tvFriends = tvFriends;
        this.tvNoFindSearch = tvNoFindSearch;
        this.imgNoFindSearch = imgNoFindSearch;
        this.edSearch = edSearch;
    }

    @NonNull
    @Override
    public AdapterSearchUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort_name, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchUser.ViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null)
            return;
        holder.tvUserName.setText(user.getFullName());
        holder.butConfirm.setVisibility(View.GONE);
        holder.tvNameFirst.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (userList == null)
            return 0;
        else
            return userList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString().trim();
                if (search.isEmpty()) {
                    // de trang recycleView
                } else {
                    List<User> userDisplay = new ArrayList<>();
                    for (User u : userList) {
                        if (u.getFullName().toLowerCase().contains(search.trim().toLowerCase())) {
                            userDisplay.add(u);
                        }
                    }
                    userList = userDisplay;
                    if (userDisplay.size() == 0) {
                        tvFriends.setVisibility(View.GONE);
                        tvNoFindSearch.setVisibility(View.VISIBLE);
                        imgNoFindSearch.setVisibility(View.VISIBLE);
                        edSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
                    } else {
                        tvFriends.setVisibility(View.VISIBLE);
                        tvNoFindSearch.setVisibility(View.GONE);
                        imgNoFindSearch.setVisibility(View.GONE);
                        edSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, R.drawable.cancel, 0);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = userList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userList = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
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
