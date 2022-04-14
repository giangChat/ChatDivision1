package com.rikkei.training.chat.ui;

import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.rikkei.training.chat.Constants;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemFriendListener;
import com.rikkei.training.chat.adapter.AdapterMessageChat;
import com.rikkei.training.chat.modle.Conversation;
import com.rikkei.training.chat.modle.Messages;
import com.rikkei.training.chat.modle.StatusFriends;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.List;

public class FragmentMessage extends Fragment {

    FirebaseDatabase db;
    FirebaseUser user;
    DatabaseReference ref;
    TextView tvMessage;
    List<User> listUser;
    List<StatusFriends> statusFriendsList;
    RecyclerView rcvListChat;
    MainActivity mainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listUser = new ArrayList<User>();
        statusFriendsList = new ArrayList<StatusFriends>();
        init(view);
        getData();
    }


    public void getData() {
        List<User> listUser1 = new ArrayList<User>();
        List<StatusFriends> statusFriendsList1 = new ArrayList<StatusFriends>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child(Constants.KEY_FRIEND).child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()
                ) {
                    StatusFriends statusFriends = dataSnapshot.getValue(StatusFriends.class);
                    statusFriendsList1.add(statusFriends);
                }
                DatabaseReference ref1 = db.getReference().child(Constants.KEY_USER);
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()
                        ) {
                            User user = dataSnapshot.getValue(User.class);
                            listUser1.add(user);
                        }
                        AdapterMessageChat adapterMessageChat =
                                new AdapterMessageChat(getConversations(listUser1, statusFriendsList1),
                                        getActivity(), new IClickItemFriendListener() {
                            @Override
                            public void onClickItemFriend(User user) {

                            }

                            @Override
                            public void onClickItemFriend(Conversation conversation) {
                                FragmentDetailMessage fragmentDetailMessage = new FragmentDetailMessage();
                                Bundle bundle = new Bundle();
                                bundle.putString("id", conversation.getId());
                                fragmentDetailMessage.setArguments(bundle);
                                mainActivity.setFragment(fragmentDetailMessage, true);
                                mainActivity.changeVisibleBottomSheet(false);
                            }
                        });
                        adapterMessageChat.notifyDataSetChanged();
                        rcvListChat.setAdapter(adapterMessageChat);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public List<Conversation> getConversations(List<User> users, List<StatusFriends> statusFriends) {
        List<StatusFriends> statusFriendsFriended = new ArrayList<>();
        List<User> listUser1 = new ArrayList<>();
        List<Conversation> conversationList = new ArrayList<>();
        List<Messages> messagesList = new ArrayList<>();
        for (StatusFriends s : statusFriends) {
            if (!s.getIdChat().equals("default") && s.getStatus().equals("friend")) {
                statusFriendsFriended.add(s);
            }
        }
        for (User u : users) {
            for (StatusFriends s : statusFriendsFriended) {
                if (u.getId().equals(s.getId())) {
                    listUser1.add(u);
                }
            }
        }
        for (StatusFriends statusFriends1 : statusFriendsFriended) {
            for (User user : listUser1) {
                if (user.getId().equals(statusFriends1.getId())) {
                    db = FirebaseDatabase.getInstance();
                    DatabaseReference refChat = db.getReference()
                            .child(Constants.KEY_CHATS).child(statusFriends1.getIdChat())
                            .child(Constants.KEY_MESSAGES);
                    refChat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()
                            ) {
                                Messages messages = dataSnapshot.getValue(Messages.class);
                                messagesList.add(messages);
                            }
                            int d = 0;
                            for (Messages m : messagesList
                            ) {
                                if (!m.isCheckSeen()) {
                                    d++;
                                }
                            }
                            Conversation conversation = new Conversation(user.getId(), user.getFullName(),
                                    user.getImgUrl(), messagesList.get(messagesList.size() - 1).getMessage(),
                                    d, messagesList.get(messagesList.size() - 1).getTimeLong());
                            conversationList.add(conversation);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        }
        return conversationList;
    }

    public void init(View view) {
        mainActivity = (MainActivity) getActivity();
        tvMessage = view.findViewById(R.id.tvMessage);
        rcvListChat = view.findViewById(R.id.rcvListChat);
    }
}
