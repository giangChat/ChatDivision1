package com.rikkei.training.chat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemFriendListener;
import com.rikkei.training.chat.adapter.AdapterFriendsFriends;
import com.rikkei.training.chat.modle.Conversation;
import com.rikkei.training.chat.modle.StatusFriends;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendFriendsFragment extends Fragment {
    List<User> userList;
    List<StatusFriends> statusFriendsList;
    View view;
    RecyclerView rcvDataFriends;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MainActivity mainActivity;
    FirebaseUser user;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        FriendFriendsFragment fragment = new FriendFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends_friends, container, false);
        init();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        getData();
        return view;
    }

    public void init() {
        mainActivity = (MainActivity) getActivity();
        rcvDataFriends = view.findViewById(R.id.rcvDataFriendsFriends);
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

                for (DataSnapshot data : snapshot.getChildren()) {
                    StatusFriends statusFriends = data.getValue(StatusFriends.class);
                    statusFriendsList.add(statusFriends);
                }
                AdapterFriendsFriends adapterFriendsFriends = new AdapterFriendsFriends(getUserFriends(userList, statusFriendsList), getContext(), new IClickItemFriendListener() {
                    @Override
                    public void onClickItemFriend(User user) {
                        FragmentDetailMessage fragmentDetailMessage = new FragmentDetailMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("id",user.getId());
                        fragmentDetailMessage.setArguments(bundle);
                        mainActivity.setFragment(fragmentDetailMessage, true);
                        mainActivity.changeVisibleBottomSheet(false);
                    }
                    @Override
                    public void onClickItemFriend(Conversation conversation) {

                    }
                });
                rcvDataFriends.setAdapter(adapterFriendsFriends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setFragment(Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment).addToBackStack(fragment.toString())
                .commit();
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

    public String getName(String fullName) {
        String[] tg = fullName.split(" ");
        return tg[tg.length - 1];
    }

    public List<User> getUserFriends(List<User> users, List<StatusFriends> statusFriends) {
        List<User> userFriends = new ArrayList<>();
        List<StatusFriends> statusFriendsFriended = new ArrayList<>();
        for (StatusFriends s : statusFriends) {
            if (s.getStatus().equals("friend")) {
                statusFriendsFriended.add(s);
            }
        }
        for (User u : users)
            for (StatusFriends s : statusFriendsFriended) {
                if (u.getId().equals(s.getId())) {
                    userFriends.add(u);
                }
            }
        return sortUser(userFriends);
    }
}
