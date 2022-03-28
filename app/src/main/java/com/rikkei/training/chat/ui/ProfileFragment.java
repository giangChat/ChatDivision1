package com.rikkei.training.chat.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rikkei.training.chat.R;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private View view;
    CircleImageView imgUser;
    TextView tvEmailUser;
    ImageView imgEditProfile,imgUser1;
    TextView tvLanguage;
    TextView tvLogout;
    ImageView imgButChooseLanguage;
    TextView tvLanguageTitle;
    TextView tvNotification;
    TextView tvVersionApp;
    private MainActivity mainActivity;
    private static final String ENGLISH_CODE = "en";
    private static final String VN_CODE = "vi";
    AlertDialog alertDialog;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        init();
        user= FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("user").child(user.getUid());
        databaseReference.child("imgUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.getValue().toString().equals("default")){
                    Glide.with(mainActivity).load(snapshot.getValue()).into(imgUser);
                    Glide.with(mainActivity).load(snapshot.getValue()).into(imgUser1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity,"Error Loading...",Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEmailUser.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getFragment(new EditProfileFragment());
                mainActivity.getBottomNavigationView().setVisibility(View.GONE);
            }
        });
        imgButChooseLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = {"",ENGLISH_CODE, VN_CODE};
                alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(getContext().getString(R.string.language))
                        .setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (strings[which].equals(ENGLISH_CODE)) {
                                    changeLanguage(ENGLISH_CODE);
                                }
                                if (strings[which].equals(VN_CODE)) {
                                    changeLanguage(VN_CODE);
                                }
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        return view;
    }

    public void init() {
        mainActivity = (MainActivity) getActivity();
        imgUser = view.findViewById(R.id.imgUser);
        imgEditProfile = view.findViewById(R.id.imgGoToEditProfile);
        tvEmailUser = view.findViewById(R.id.tvEmailUser);
        tvLanguage = view.findViewById(R.id.tvLanguage);
        tvLogout = view.findViewById(R.id.tvLogout);
        imgButChooseLanguage = view.findViewById(R.id.imgButChooseLanguage);
        tvNotification = view.findViewById(R.id.tvNotification);
        tvLanguageTitle = view.findViewById(R.id.tvLanguageTitle);
        tvVersionApp = view.findViewById(R.id.tvVersionApp);
        imgUser1=view.findViewById(R.id.imgUser1);
    }

    public void changeLanguage(String language) {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Locale locale = new Locale(language);
                Locale.setDefault(locale);
                Resources resources = mainActivity.getResources();
                Configuration configuration = mainActivity.getResources().getConfiguration();
                configuration.setLocale(locale);
                mainActivity.createConfigurationContext(configuration);
                resources.updateConfiguration(configuration,resources.getDisplayMetrics());
                if (language.equals(ENGLISH_CODE))
                    tvLanguage.setText(resources.getString(R.string.English));
                else
                    tvLanguage.setText(resources.getString(R.string.VietNamese));
            }
        },1000);
        Intent intent = mainActivity.getIntent();
        mainActivity.finish();
        startActivity(intent);
        mainActivity.getFragment(new ProfileFragment());
    }
}
