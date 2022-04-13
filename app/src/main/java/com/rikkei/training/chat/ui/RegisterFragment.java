package com.rikkei.training.chat.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.modle.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class RegisterFragment extends Fragment {

    TextView tvDK;
    ImageView imgBack;
    Button btDK;
    EditText edtName, edtEmail, edtPass;
    CheckBox checkBox_DK;
    LoginRegisterActivity loginRegisterActivity;
    FirebaseDatabase db;
    DatabaseReference ref;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loginRegisterActivity = (LoginRegisterActivity) getActivity();
        btDK.setEnabled(false);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
        btDK.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (check(edtName.getText().toString(), edtEmail.getText().toString(),
                        edtPass.getText().toString()) && checkBox_DK.isChecked()) {
                    btDK.setBackgroundResource(R.drawable.btn_customer_full);
                    btDK.setEnabled(true);
                } else {
                    btDK.setBackgroundResource(R.drawable.btn_customer);
                    btDK.setEnabled(false);
                }

            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (check(edtName.getText().toString(), edtEmail.getText().toString(),
                        edtPass.getText().toString()) && checkBox_DK.isChecked()) {
                    btDK.setBackgroundResource(R.drawable.btn_customer_full);
                    btDK.setEnabled(true);
                } else {
                    btDK.setBackgroundResource(R.drawable.btn_customer);
                    btDK.setEnabled(false);
                }
            }
        });

        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (check(edtName.getText().toString(), edtEmail.getText().toString(),
                        edtPass.getText().toString()) && checkBox_DK.isChecked()) {
                    btDK.setBackgroundResource(R.drawable.btn_customer_full);
                    btDK.setEnabled(true);
                } else {
                    btDK.setBackgroundResource(R.drawable.btn_customer);
                    btDK.setEnabled(false);
                }
            }
        });
        checkBox_DK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (check(edtName.getText().toString(), edtEmail.getText().toString(),
                        edtPass.getText().toString()) && checkBox_DK.isChecked()) {
                    btDK.setBackgroundResource(R.drawable.btn_customer_full);
                    btDK.setEnabled(true);
                } else {
                    btDK.setBackgroundResource(R.drawable.btn_customer);
                    btDK.setEnabled(false);
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRegisterActivity.goToFragment(new LoginFragment());
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onClickSignUp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            User user1 = new User(user.getUid(), edtName.getText().toString(),
                                    "default", "default", "default", edtEmail.getText().toString(),
                                    edtPass.getText().toString());
                            ref.child("user").child(user.getUid()).setValue(user1);

                        } else {
                            Toast.makeText(getActivity(), "NO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean check(String name, String email, String pass) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass);
    }

    public void init(View view) {
        imgBack = view.findViewById(R.id.imgBack);
        btDK = view.findViewById(R.id.btDK);
        tvDK = view.findViewById(R.id.tvDK);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPass = view.findViewById(R.id.edtPassWord);
        edtName = view.findViewById(R.id.edtName);
        checkBox_DK = view.findViewById(R.id.chbPolicy);
    }
}