package com.rikkei.training.chat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rikkei.training.chat.R;
import com.rikkei.training.chat.adapter.AdapterListChat;
import com.rikkei.training.chat.modle.Conversation;
import com.rikkei.training.chat.modle.StatusFriends;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.List;

public class fragmentMessage extends Fragment {

    List<User> listChat = new ArrayList<User>();
    List<StatusFriends> statusFriendsList = new ArrayList<StatusFriends>();
    RecyclerView rcvListChat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getData();
    }

    public void init(View view) {
        rcvListChat = view.findViewById(R.id.rcvListChat);
    }

    public void getData() {

        User user1 = new User("1", "Vi Nhật Giang", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        User user2 = new User("2", "Vi Nhật Khang", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        User user3 = new User("3", "Vi Nhật Khanh", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        User user4 = new User("4", "Vi Nhật Khoa", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        User user5 = new User("5", "Vi Nhật Hoàng", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        User user6 = new User("6", "Vi Nhật Anh", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        User user7 = new User("7", "Vi Trúc Ly", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        User user8 = new User("8", "Vi Nhật X", "0399434998",
                "05/06/2000", "", "vinhatgiang@gmail.com", "123456");
        listChat.add(user1);
        listChat.add(user2);
        listChat.add(user3);
        listChat.add(user4);
        listChat.add(user5);
        listChat.add(user6);
        listChat.add(user7);
        listChat.add(user8);

        StatusFriends statusFriends1 = new StatusFriends("2", "1", "default");
        StatusFriends statusFriends2 = new StatusFriends("5", "1", "dd");
        StatusFriends statusFriends3 = new StatusFriends("9", "1", "dd");
        StatusFriends statusFriends4 = new StatusFriends("7", "1", "dd");
        StatusFriends statusFriends5 = new StatusFriends("1", "1", "dd");
        StatusFriends statusFriends6 = new StatusFriends("3", "1", "dd");
        StatusFriends statusFriends7 = new StatusFriends("2", "1", "default");
        StatusFriends statusFriends8 = new StatusFriends("5", "1", "dd");
        StatusFriends statusFriends9 = new StatusFriends("9", "1", "dd");
        StatusFriends statusFriends10 = new StatusFriends("7", "1", "dd");
        StatusFriends statusFriends11 = new StatusFriends("1", "1", "dd");
        StatusFriends statusFriends12 = new StatusFriends("3", "1", "dd");
        StatusFriends statusFriends13 = new StatusFriends("2", "1", "default");
        StatusFriends statusFriends14 = new StatusFriends("5", "1", "dd");
        StatusFriends statusFriends15 = new StatusFriends("9", "1", "dd");
        StatusFriends statusFriends16 = new StatusFriends("7", "1", "dd");
        StatusFriends statusFriends17 = new StatusFriends("1", "1", "dd");
        StatusFriends statusFriends18 = new StatusFriends("3", "1", "dd");

        statusFriendsList.add(statusFriends1);
        statusFriendsList.add(statusFriends2);
        statusFriendsList.add(statusFriends3);
        statusFriendsList.add(statusFriends4);
        statusFriendsList.add(statusFriends5);
        statusFriendsList.add(statusFriends6);
        statusFriendsList.add(statusFriends7);
        statusFriendsList.add(statusFriends8);
        statusFriendsList.add(statusFriends9);
        statusFriendsList.add(statusFriends10);
        statusFriendsList.add(statusFriends11);
        statusFriendsList.add(statusFriends12);
        statusFriendsList.add(statusFriends13);
        statusFriendsList.add(statusFriends14);
        statusFriendsList.add(statusFriends15);
        statusFriendsList.add(statusFriends16);
        statusFriendsList.add(statusFriends17);
        statusFriendsList.add(statusFriends18);

    }

    public List<Conversation> getConversations(List<User> users, List<StatusFriends> statusFriends) {
        List<StatusFriends> statusFriendsFriended = new ArrayList<>();
        List<User> listChat = new ArrayList<>();
        for (StatusFriends s : statusFriends) {
            if (!s.getIdChat().equals("default")) {
                statusFriendsFriended.add(s);
            }
        }
        for (User u : users)
            for (StatusFriends s : statusFriendsFriended) {
                if (u.getId().equals(s.getId())) {
                    listChat.add(u);
                }
            }
        return null;
    }
}
