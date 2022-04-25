package com.rikkei.training.chat.ui;

import android.os.Bundle;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentMessage extends Fragment {

    FirebaseDatabase db;
    FirebaseUser userM;
    DatabaseReference ref;
    List<User> listUser;
    TextView textView;
    List<StatusFriends> statusFriendsList;
    RecyclerView rcvListChat;
    MainActivity mainActivity;
    AdapterMessageChat adapterMessageChat;
    List<Conversation> conversationList = new ArrayList<>();
    String idSent;

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
        userM = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        idSent = userM.getUid();
        init(view);
        getData();
    }


    public void getData() {
        DatabaseReference ref = db.getReference().child(Constants.KEY_FRIEND).child(userM.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()
                ) {
                    StatusFriends statusFriends = dataSnapshot.getValue(StatusFriends.class);
                    statusFriendsList.add(statusFriends);
                }
                DatabaseReference ref1 = db.getReference().child(Constants.KEY_USER);
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()
                        ) {
                            User user = dataSnapshot.getValue(User.class);
                            listUser.add(user);
                        }
                        getListConversations(listUser, statusFriendsList);
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

    public void getListConversations(List<User> users, List<StatusFriends> statusFriends) {
        List<StatusFriends> statusFriendsFriended = new ArrayList<>();
        List<User> listUser1 = new ArrayList<>();
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
                if (statusFriends1.getId().equals(user.getId())) {
                    db = FirebaseDatabase.getInstance();
                    DatabaseReference refChat = db.getReference()
                            .child(Constants.KEY_CHATS).child(statusFriends1.getIdChat())
                            .child(Constants.KEY_MESSAGES);
                    refChat.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            messagesList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()
                            ) {
                                Messages messages = dataSnapshot.getValue(Messages.class);
                                messagesList.add(messages);
                            }
                            int d = 0;
                            for (Messages m : messagesList) {
                                if (!m.getIdSender().equals(userM.getUid()) && !m.isCheckSeen()) {
                                    d++;
                                }
                            }
                            if (messagesList.get(messagesList.size() - 1).getIdSender().equals(userM.getUid())
                                    && messagesList.get(messagesList.size() - 1).getType().equals("img")) {

                                Conversation conversationPhoto = new Conversation();
                                for (int i = 0; i < conversationList.size(); i++) {
                                    if (statusFriends1.getId().equals(conversationList.get(i).getId())) {
                                        conversationPhoto = conversationList.get(i);
                                    }
                                }
                                if (conversationList.contains(conversationPhoto)) {
                                    conversationList.set(conversationList.indexOf(conversationPhoto),
                                            new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                                    user.getImgUrl(), messagesList.get(messagesList.size() - 1).getMessage(),
                                                    d, messagesList.get(messagesList.size() - 1).getTimeLong()));
                                } else {
                                    Conversation conversation = new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                            user.getImgUrl(), "Bạn đã gửi một ảnh",
                                            d, messagesList.get(messagesList.size() - 1).getTimeLong());
                                    conversationList.add(conversation);
                                }
                            } else if (messagesList.get(messagesList.size() - 1).getIdSender().equals(userM.getUid())
                                    && messagesList.get(messagesList.size() - 1).getType().equals("emoji")) {

                                Conversation conversationSticker = new Conversation();
                                for (int i = 0; i < conversationList.size(); i++) {
                                    if (statusFriends1.getId().equals(conversationList.get(i).getId())) {
                                        conversationSticker = conversationList.get(i);
                                    }
                                }
                                if (conversationList.contains(conversationSticker)) {
                                    conversationList.set(conversationList.indexOf(conversationSticker),
                                            new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                                    user.getImgUrl(), messagesList.get(messagesList.size() - 1).getMessage(),
                                                    d, messagesList.get(messagesList.size() - 1).getTimeLong()));
                                } else {
                                    Conversation conversation = new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                            user.getImgUrl(), "Bạn đã gửi một Sticker",
                                            d, messagesList.get(messagesList.size() - 1).getTimeLong());
                                    conversationList.add(conversation);
                                }

                            } else if (messagesList.get(messagesList.size() - 1).getIdSender().equals(userM.getUid())) {

                                Conversation conversationS = new Conversation();
                                for (int i = 0; i < conversationList.size(); i++) {
                                    if (messagesList.get(messagesList.size() - 1).getTimeLong() == conversationList.get(i).getLastTime()) {
                                        conversationS = conversationList.get(i);
                                    }
                                }
                                if (conversationList.contains(conversationS)) {

                                    conversationList.set(conversationList.indexOf(conversationS),
                                            new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                                    user.getImgUrl(), "Bạn : " + messagesList.get(messagesList.size() - 1).getMessage(),
                                                    d, messagesList.get(messagesList.size() - 1).getTimeLong()));

                                } else {
                                    Conversation conversation = new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                            user.getImgUrl(), "Bạn : " + messagesList.get(messagesList.size() - 1).getMessage(),
                                            d, messagesList.get(messagesList.size() - 1).getTimeLong());
                                    conversationList.add(conversation);
                                }

                            } else {
                                Conversation conversationT = new Conversation();
                                for (int i = 0; i < conversationList.size(); i++) {
                                    if (statusFriends1.getId().equals(conversationList.get(i).getId())) {
                                        conversationT = conversationList.get(i);
                                    }
                                }
                                if (conversationList.contains(conversationT)) {
                                    conversationList.set(conversationList.indexOf(conversationT),
                                            new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                                    user.getImgUrl(), messagesList.get(messagesList.size() - 1).getMessage(),
                                                    d, messagesList.get(messagesList.size() - 1).getTimeLong()));
                                } else {
                                    Conversation conversation = new Conversation(user.getId(), user.getFullName(), statusFriends1.getIdChat(),
                                            user.getImgUrl(), messagesList.get(messagesList.size() - 1).getMessage(),
                                            d, messagesList.get(messagesList.size() - 1).getTimeLong());
                                    conversationList.add(conversation);
                                }
                            }
                            adapterMessageChat.notifyDataSetChanged();
                            List<Conversation> conversationList1 = new ArrayList<>();
                            conversationList1.addAll(conversationList);
                            conversationList.clear();
                            conversationList.addAll(sortConversation(conversationList1));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        }
    }

    private void updateMessage(String idRoomChat) {
        DatabaseReference refM = db.getReference().child(Constants.KEY_CHATS)
                .child(idRoomChat).child(Constants.KEY_MESSAGES);
        refM.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (!data.child("idSender").getValue(String.class).equals(idSent) && !data.child("checkSeen").getValue(Boolean.class)) {
                        refM.child(data.getKey()).child("checkSeen").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private List<Conversation> sortConversation(List<Conversation> conversationList) {
        List<Conversation> lstConversations = new ArrayList<>();
        Comparator comparator = (Comparator<Conversation>) (o1, o2) -> Long.compare(o2.getLastTime(), o1.getLastTime());
        Collections.sort(conversationList, comparator);
        lstConversations.addAll(conversationList);
        return lstConversations;
    }

    public void init(View view) {
        textView = view.findViewById(R.id.tvMessage);
        mainActivity = (MainActivity) getActivity();
        rcvListChat = view.findViewById(R.id.rcvListChat);
        conversationList.clear();
        adapterMessageChat = new AdapterMessageChat(conversationList,
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
                updateMessage(conversation.getIdRoomChat());
            }
        });
        rcvListChat.setAdapter(adapterMessageChat);
    }
}
