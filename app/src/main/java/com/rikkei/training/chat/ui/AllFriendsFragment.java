package com.rikkei.training.chat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    AdapterAllFriends adapterAllFriends;

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },2000);
        return view;
    }

    public void init() {
        rcvDataAllFriends = view.findViewById(R.id.rcvDataAllFriends);
    }

    public void getData() {
        userList = new ArrayList<>();
        statusFriendsList=new ArrayList<>();
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
                statusFriendsList.clear();
                Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                for (DataSnapshot data : dataSnapshotIterable) {
                    StatusFriends statusFriends = data.getValue(StatusFriends.class);
                    statusFriendsList.add(statusFriends);
                }
                userList = sortUser(userList);
                adapterAllFriends = new AdapterAllFriends(userList, statusFriendsList, getContext());
                rcvDataAllFriends.setAdapter(adapterAllFriends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            if (!u.getId().equals(user.getUid()))
                users.add(u);
        }
        return users;
    }

    public String getName(String fullName) {
        String[] tg = fullName.split(" ");
        return tg[tg.length - 1];
    }
}