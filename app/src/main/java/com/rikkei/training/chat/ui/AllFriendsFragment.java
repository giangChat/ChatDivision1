package com.rikkei.training.chat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rikkei.training.chat.R;
import com.rikkei.training.chat.adapter.AdapterAllFriends;
import com.rikkei.training.chat.modle.StatusFriends;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AllFriendsFragment extends Fragment {
    List<User> userList;
    List<StatusFriends> statusFriendsList;
    View view;
    RecyclerView rcvDataAllFriends;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        AllFriendsFragment fragment = new AllFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_friends, container, false);
        init();
        getData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvDataAllFriends.setLayoutManager(layoutManager);
        AdapterAllFriends adapterAllFriends = new AdapterAllFriends(getAllFriends(), getFriendsFriends(), getContext());
        rcvDataAllFriends.setAdapter(adapterAllFriends);
        return view;
    }

    public void init() {
        rcvDataAllFriends = view.findViewById(R.id.rcvDataAllFriends);
    }

    public void getData() {
        //TODO Remove
        statusFriendsList = new ArrayList<>();
        userList = new ArrayList<>();
        User user = new User("Vu Giang", "", "", "", "vugiang@gmail.com", "123");
        User user1 = new User("Vu Anh", "", "", "", "vuanh@gmail.com", "123");
        User user2 = new User("Vu An", "", "", "", "vuan@gmail.com", "123");
        User user3 = new User("Vu Duc", "", "", "", "vuduc@gmail.com", "123");
        User user4 = new User("Vu Hoa", "", "", "", "vuhoa@gmail.com", "123");
        User user5 = new User("Vu Ha", "", "", "", "vuha@gmail.com", "123");
        User user6 = new User("Vu Cuong", "", "", "", "vucuong@gmail.com", "123");
        User user7 = new User("Vu Tung", "", "", "", "vutung@gmail.com", "123");
        User user8 = new User("Vu Manh", "", "", "", "vumanh@gmail.com", "123");
        User user9 = new User("Vu Kien", "", "", "", "vukien@gmail.com", "123");
        User user10 = new User("Vu Tuan", "", "", "", "vutuan@gmail.com", "123");
        userList.add(user);
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        userList.add(user6);
        userList.add(user7);
        userList.add(user8);
        userList.add(user9);
        userList.add(user10);
        StatusFriends statusFriends = new StatusFriends("vuanh@gmail.com", "friend");
        StatusFriends statusFriends1 = new StatusFriends("vugiang@gmail.com", "invite friend");
        StatusFriends statusFriends2 = new StatusFriends("vuanh@gmail.com", "agree");
        StatusFriends statusFriends3 = new StatusFriends("vucuong@gmail.com", "agree");
        StatusFriends statusFriends4 = new StatusFriends("vuha@gmail.com", "friend");
        StatusFriends statusFriends5 = new StatusFriends("vutuan@gmail.com", "friend");
        StatusFriends statusFriends6 = new StatusFriends("vukien@gmail.com", "friend");
        StatusFriends statusFriends7 = new StatusFriends("vutung@gmail.com", "friend");
        statusFriendsList.add(statusFriends);
        statusFriendsList.add(statusFriends1);
        statusFriendsList.add(statusFriends2);
        statusFriendsList.add(statusFriends3);
        statusFriendsList.add(statusFriends4);
        statusFriendsList.add(statusFriends5);
        statusFriendsList.add(statusFriends6);
        statusFriendsList.add(statusFriends7);
    }

    public List<User> sortUser(List<User> userList) {
        List<User> users = new ArrayList<>();
        Comparator<User> comparator = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return getName(o1.getFullName()).compareTo(getName(o2.getFullName()));
            }
        };
        Collections.sort(userList, comparator);
        for (User u : userList) {
            users.add(u);
        }
        return users;
    }

    public List<StatusFriends> getFriendsFriends() {
        List<StatusFriends> statusFriendsList1 = new ArrayList<>();
        for (StatusFriends s : statusFriendsList) {
            if (s.getStatus().equals("friend"))
                statusFriendsList1.add(s);
        }
        return statusFriendsList1;
    }

    public List<User> getAllFriends() {
        return sortUser(userList);
    }

    public String getName(String fullName) {
        String[] tg = fullName.split(" ");
        return tg[tg.length - 1];
    }
}
