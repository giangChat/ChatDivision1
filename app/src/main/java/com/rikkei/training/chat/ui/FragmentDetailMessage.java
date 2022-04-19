package com.rikkei.training.chat.ui;


import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rikkei.training.chat.Constants;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.a.IClickItemDetailMessage;
import com.rikkei.training.chat.adapter.AdapterDetailMessage;
import com.rikkei.training.chat.adapter.AdapterPhoto;
import com.rikkei.training.chat.adapter.AdapterSticker;
import com.rikkei.training.chat.modle.Messages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentDetailMessage extends Fragment {

    RecyclerView rcvMessage, rcvImg;
    TextView tvTimeSendMessage;
    TextView tvUserName;
    EditText edMessage;
    ImageView imgSend, imgAvatar, imgBack, imgSticker, imgPhoto;
    FirebaseDatabase db;
    FirebaseUser user;
    DatabaseReference ref;
    String imgUrlReceived = "";
    String idReceived;
    String idSend;
    String idRoomChat;
    MainActivity mainActivity;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference riversRef;
    AdapterDetailMessage adapterDetailMessage;
    List<Messages> messagesList;
    boolean checkSticker = true;
    boolean checkPhoto = true;
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
        idSend = user.getUid();
        db = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        riversRef = storageReference.child("imagesMessage/").child(String.valueOf(now.getTime()));
        getMessage();

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

        imgSend.setOnClickListener(v -> {
            //messagesList.clear();
            //updateMessage();
            createMessage("text", null, null);
            edMessage.setText("");
        });

        imgBack.setOnClickListener(v -> {
            mainActivity.getSupportFragmentManager().popBackStack();
            mainActivity.changeVisibleBottomSheet(true);

        });

        imgSticker.setOnClickListener(v -> {
            if (checkSticker) {
                String PACKAGE_NAME = mainActivity.getApplicationContext().getPackageName();
                List<Integer> integers = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    int in = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + "emoji" + i, null, null);
                    integers.add(in);
                }
                AdapterSticker adapterSticker = new AdapterSticker(mainActivity, integers, emoji -> createMessage("emoji", String.valueOf(emoji), null));
                rcvImg.setAdapter(adapterSticker);
                imgSticker.setImageResource(R.drawable.ic_emoji_blue_icon);
                rcvImg.setVisibility(View.VISIBLE);
                mainActivity.hideKeyboard(view);
                rcvMessage.scrollToPosition(messagesList.size() - 1);
                imgPhoto.setImageResource(R.drawable.ic_photo_local_gray_icon);
                checkSticker = false;
                checkPhoto = true;
            } else {
                rcvImg.setVisibility(View.GONE);
                imgSticker.setImageResource(R.drawable.ic_emoji_gray_icon);
                checkSticker = true;
            }
        });
        imgPhoto.setOnClickListener(v -> {
            if (checkPhoto) {
                List<Uri> lstUris = new ArrayList<>();
                if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
                }
                String[] projection = {
                        MediaStore.Images.ImageColumns._ID,
                };
                Cursor cursor = mainActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                        lstUris.add(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                AdapterPhoto adapterPhoto = new AdapterPhoto(lstUris, uri -> {

                    riversRef.putFile(uri).addOnSuccessListener(taskSnapshot -> riversRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        String url = uri1.toString();
                        createMessage("img", null, url);
                    })).addOnFailureListener(e -> Toast.makeText(mainActivity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show());
                });

                rcvImg.setAdapter(adapterPhoto);
                rcvImg.setVisibility(View.VISIBLE);
                rcvMessage.scrollToPosition(messagesList.size() - 1);
                imgPhoto.setImageResource(R.drawable.ic_photo_local_blue_icon);
                imgSticker.setImageResource(R.drawable.ic_emoji_gray_icon);
                checkSticker = true;
                checkPhoto = false;
            } else {
                checkPhoto = true;
                imgPhoto.setImageResource(R.drawable.ic_photo_local_gray_icon);
                imgSticker.setImageResource(R.drawable.ic_emoji_gray_icon);
                rcvImg.setVisibility(View.GONE);
            }
        });
    }

    private void createMessage(String type, String codeEmoji, String url) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String message = "";
        if (type.equals("text")) {
            message = edMessage.getText().toString().trim();
        } else if (type.equals("emoji")) {
            message = codeEmoji;
        } else if (type.equals("img")) {
            message = url;
        }
        long time = now.getTime();
        Messages messageS = new Messages(false, idSend, message, 0, time, type);
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
                    chatRef.child(Constants.KEY_CHATS).child(snapshot.getValue().toString()).child(Constants.KEY_MESSAGES).push().setValue(messageS);
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
                            messagesList.clear();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Messages message = data.getValue(Messages.class);
                                messagesList.add(message);
                            }
                            tvTimeSendMessage.setText(messagesList.get(messagesList.size() - 1).setTextTimeSendMessage());
                            List<Messages> messagesListHandel = Messages.handle(messagesList);
                            messagesList.clear();
                            messagesList.addAll(messagesListHandel);
                            rcvMessage.scrollToPosition(messagesList.size() - 1);
                            adapterDetailMessage = new AdapterDetailMessage(messagesList, imgUrlReceived, mainActivity, () -> {
                                rcvImg.setVisibility(View.GONE);
                                mainActivity.hideKeyboard(getView());
                                imgSticker.setImageResource(R.drawable.ic_emoji_gray_icon);
                            });
                            adapterDetailMessage.notifyDataSetChanged();
                            rcvMessage.setAdapter(adapterDetailMessage);
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

    private void updateMessage() {
        ref = db.getReference()
                .child(Constants.KEY_FRIEND)
                .child(idSend)
                .child(idReceived)
                .child(Constants.KEY_ID_CHAT);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idRoomChat = snapshot.getValue().toString();
                DatabaseReference refM = db.getReference().child(Constants.KEY_CHATS)
                        .child(idRoomChat).child(Constants.KEY_MESSAGES);
                refM.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (!data.child("idSender").getValue(String.class).equals(idSend) && !data.child("checkSeen").getValue(Boolean.class)) {
                                refM.child(data.getKey()).child("checkSeen").setValue(true);
                            }
                        }
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

    public void init(View view) {

        mainActivity = (MainActivity) getActivity();
        imgSticker = view.findViewById(R.id.imgSticker);
        imgPhoto = view.findViewById(R.id.imgPhoto);
        imgBack = view.findViewById(R.id.imgBack);
        rcvMessage = view.findViewById(R.id.rcvDetailMessage);
        tvUserName = view.findViewById(R.id.tvUserName);
        edMessage = view.findViewById(R.id.edMessage);
        imgSend = view.findViewById(R.id.imgSend);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvTimeSendMessage = view.findViewById(R.id.tvTimeSendMessage);
        rcvImg = view.findViewById(R.id.rcvImg);
    }
}
