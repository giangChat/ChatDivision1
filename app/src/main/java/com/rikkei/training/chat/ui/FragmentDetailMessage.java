package com.rikkei.training.chat.ui;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rikkei.training.chat.Constants;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemDetailMessage;
import com.rikkei.training.chat.a.IClickItemEmojiListener;
import com.rikkei.training.chat.adapter.AdapterDetailMessage;
import com.rikkei.training.chat.adapter.AdapterEmoji;
import com.rikkei.training.chat.modle.Messages;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentDetailMessage extends Fragment {

    RecyclerView rcvMessage, rcvImgEmoji;
    TextView tvTimeSendMessage;
    TextView tvUserName;
    EditText edMessage;
    ImageView imgSend, imgAvatar, imgBack, imgEmoji;
    FirebaseDatabase db;
    FirebaseUser user;
    DatabaseReference ref;
    String imgUrlReceived = "";
    String idReceived;
    String idSend;
    String idRoomChat;
    MainActivity mainActivity;
    AdapterDetailMessage adapterDetailMessage;
    List<Messages> messagesList;
    Date now = new Date();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        init(view);
        if (bundle != null) {
            idReceived = bundle.getString("id");
        }
        messagesList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        getMessage();
        adapterDetailMessage = new AdapterDetailMessage(messagesList, imgUrlReceived, mainActivity, new IClickItemDetailMessage() {
            @Override
            public void onClickItemDetailMessage() {
                rcvImgEmoji.setVisibility(View.GONE);
                mainActivity.hideKeyboard(view);
                imgEmoji.setImageResource(R.drawable.ic_emoji_gray_icon);
            }

            @Override
            public void onClickItemTextlMessage() {

            }
        });
        rcvMessage.setAdapter(adapterDetailMessage);
        ref = db.getReference().child(Constants.KEY_USER).child(idReceived);
        ref.child(Constants.KEY_IMG).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().equals("default")) {
                    Glide.with(mainActivity).load(snapshot.getValue()).into(imgAvatar);
                    imgUrlReceived = snapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, "Error Loading...", Toast.LENGTH_SHORT).show();
            }
        });

        ref.child(Constants.KEY_FULL_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvUserName.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, "Error Loading...", Toast.LENGTH_SHORT).show();
            }
        });
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(edMessage.getText().toString())) {
                    imgSend.setVisibility(View.VISIBLE);
                } else {
                    imgSend.setVisibility(View.GONE);
                }
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesList.clear();
                createMessage("text",null);
                edMessage.setText("");
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
                mainActivity.changeVisibleBottomSheet(true);

            }
        });
        imgEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PACKAGE_NAME = mainActivity.getApplicationContext().getPackageName();
                List<Integer> integers = new ArrayList<>();
                for(int i = 0; i < 10; i++){
                    int in = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+"emoji"+i,null, null);
                    integers.add(in);
                }
                AdapterEmoji adapterEmoji = new AdapterEmoji(mainActivity, integers, new IClickItemEmojiListener() {
                    @Override
                    public void onClickItemEmoji(int emoji) {
                        createMessage("emoji",String.valueOf(emoji));
                    }
                });

                rcvImgEmoji.setAdapter(adapterEmoji);
                imgEmoji.setImageResource(R.drawable.ic_emoji_blue_icon);
                rcvImgEmoji.setVisibility(View.VISIBLE);
                mainActivity.hideKeyboard(view);
                rcvMessage.scrollToPosition(messagesList.size() - 1);

            }
        });

    }

    private void createMessage(String type, String codeEmoji) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String message ="";
        if(type.equals("text")){
            message = edMessage.getText().toString().trim();
        }else if(type.equals("emoji")){
            message = codeEmoji;
        }
        idSend = user.getUid();
        long time = now.getTime();
        Messages messages = new Messages(false, idSend, message, 0, time, type);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference()
                .child(Constants.KEY_FRIEND)
                .child(idSend)
                .child(idReceived)
                .child(Constants.KEY_ID_CHAT);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference chatRef = db.getReference();
                if (snapshot.getValue().toString().equals("default")) {
                    idRoomChat = idSend + idReceived;
                    chatRef.child(Constants.KEY_FRIEND).child(idSend).child(idReceived).child(Constants.KEY_ID_CHAT).setValue(idRoomChat);
                    chatRef.child(Constants.KEY_FRIEND).child(idReceived).child(idSend).child(Constants.KEY_ID_CHAT).setValue(idRoomChat);
                } else {
                    chatRef.child(Constants.KEY_CHATS).child(snapshot.getValue().toString()).child(Constants.KEY_MESSAGES).push().setValue(messages);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getMessage() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        idSend = user.getUid();
        idRoomChat = "";
        db = FirebaseDatabase.getInstance();
        ref = db.getReference()
                .child(Constants.KEY_FRIEND)
                .child(idSend)
                .child(idReceived).child(Constants.KEY_ID_CHAT);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().equals("default")) {
                    idRoomChat = snapshot.getValue().toString();
                    DatabaseReference chatRef = db.getReference()
                            .child(Constants.KEY_CHATS)
                            .child(idRoomChat)
                            .child(Constants.KEY_MESSAGES);
                    chatRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Messages message = data.getValue(Messages.class);
                                messagesList.add(message);
                            }
                            List<Messages>  messagesListHandel = Messages.handle(messagesList);
                            messagesList.clear();
                            messagesList.addAll(messagesListHandel);
                            tvTimeSendMessage.setText(Messages.setTextTimeSendMessage(messagesList.get(messagesList.size() - 1).getTimeLong()));
                            adapterDetailMessage.notifyDataSetChanged();
                            rcvMessage.scrollToPosition(messagesList.size() - 1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mainActivity, "Error Loading...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, "Error Loading...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init(View view) {

        mainActivity = (MainActivity) getActivity();
        imgEmoji = view.findViewById(R.id.imgEmoji);
        imgBack = view.findViewById(R.id.imgBack);
        rcvMessage = view.findViewById(R.id.rcvDetailMessage);
        tvUserName = view.findViewById(R.id.tvUserName);
        edMessage = view.findViewById(R.id.edMessage);
        imgSend = view.findViewById(R.id.imgSend);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvTimeSendMessage = view.findViewById(R.id.tvTimeSendMessage);
        rcvImgEmoji = view.findViewById(R.id.rcvImgEmoji);
    }
}
