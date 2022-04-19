package com.rikkei.training.chat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.adapter.AdapterAllFriends;
import com.rikkei.training.chat.adapter.AdapterInviteFriends;
import com.rikkei.training.chat.adapter.AdapterReqAgreeFriends;
import com.rikkei.training.chat.modle.StatusFriends;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReqFriendsFragment extends Fragment {

    View view;
    RecyclerView rcvDataReqAgreeFriends;
    RecyclerView rcvDataReqInvitedFriends;
    List<User> userList;
    List<StatusFriends> statusFriendsList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FriendsFragment friendsFragment;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        ReqFriendsFragment fragment = new ReqFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_req_friends, container, false);
        init();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        getData();

        return view;
    }

    public void init() {
        friendsFragment = (FriendsFragment) getParentFragment();
        rcvDataReqInvitedFriends = view.findViewById(R.id.rcvDataReqInvitedFriend);
        rcvDataReqAgreeFriends = view.findViewById(R.id.rcvDataReqAgreeFriend);
    }

    public void getData() {
        userList = new ArrayList<>();
        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                for (DataSnapshot data : dataSnapshotIterable) {
                    User user1 = data.getValue(User.class);
                    if (!user1.getId().equals(user.getUid()))
                        userList.add(user1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        statusFriendsList = new ArrayList<>();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference();
        databaseReference1.child("friend").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                statusFriendsList.clear();
                for (DataSnapshot data : dataSnapshotIterable) {
                    StatusFriends statusFriends = data.getValue(StatusFriends.class);
                    statusFriendsList.add(statusFriends);
                }
                AdapterInviteFriends adapterInviteFriends = new AdapterInviteFriends(getUserFriendsReqInviteFriend(userList, statusFriendsList), getContext());
                rcvDataReqInvitedFriends.setAdapter(adapterInviteFriends);
                AdapterReqAgreeFriends adapterReqAgreeFriends = new AdapterReqAgreeFriends(getUserFriendsReqAgree(userList, statusFriendsList), getContext());
                rcvDataReqAgreeFriends.setAdapter(adapterReqAgreeFriends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public List<User> sortUser(List<User> userList) {
        List<User> users = new ArrayList<>();
        Comparator<User> comparator = (o1, o2) -> getName(o1.getFullName()).compareTo(getName(o2.getFullName()));
        Collections.sort(userList, comparator);
        for (User u : userList) {
            users.add(u);
        }
        return users;
    }

    public List<User> getUserFriendsReqAgree(List<User> users, List<StatusFriends> statusFriendsList1) {
        List<User> userFriendsReqAgree = new ArrayList<>();
        List<StatusFriends> statusFriendsFriended = new ArrayList<>();
        for (StatusFriends s : statusFriendsList) {
            if (s.getStatus().equals("agree")) {
                statusFriendsFriended.add(s);
            }
        }
        for (User u : userList)
            for (StatusFriends s : statusFriendsFriended) {
                if (u.getId().equals(s.getId())) {
                    userFriendsReqAgree.add(u);
                }
            }
        return sortUser(userFriendsReqAgree);
    }

    public List<User> getUserFriendsReqInviteFriend(List<User> users, List<StatusFriends> statusFriendsList1) {
        List<User> userFriendsReqInviteFriend = new ArrayList<>();
        List<StatusFriends> statusFriendsFriended = new ArrayList<>();
        for (StatusFriends s : statusFriendsList1) {
            if (s.getStatus().equals("invite friend")) {
                statusFriendsFriended.add(s);
            }
        }
        for (User u : users)
            for (StatusFriends s : statusFriendsFriended) {
                if (u.getId().equals(s.getId())) {
                    userFriendsReqInviteFriend.add(u);
                }
            }
        return sortUser(userFriendsReqInviteFriend);
    }

    public String getName(String fullName) {
        String[] tg = fullName.split(" ");
        return tg[tg.length - 1];
    }
}
