package com.rikkei.training.chat.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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



public class LoginFragment extends Fragment {
    TextView tvDK;
    Button btDN;
    EditText edtEmail, edtPass;
    FirebaseDatabase db;
    DatabaseReference ref;
    LoginRegisterActivity loginRegisterActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
        loginRegisterActivity = (LoginRegisterActivity) getActivity();

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (check(edtEmail.getText().toString(), edtPass.getText().toString())) {
                    btDN.setBackgroundResource(R.drawable.btn_customer_full);
                    btDN.setEnabled(true);
                } else {
                    btDN.setBackgroundResource(R.drawable.btn_customer);
                    btDN.setEnabled(false);
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
                if (check(edtEmail.getText().toString(), edtPass.getText().toString())) {
                    btDN.setBackgroundResource(R.drawable.btn_customer_full);
                    btDN.setEnabled(true);
                } else {
                    btDN.setBackgroundResource(R.drawable.btn_customer);
                    btDN.setEnabled(false);

                }
            }
        });
        btDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onClickLogin();
            }

        });
        tvDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRegisterActivity.goToFragment(new RegisterFragment());
            }
        });

    }

    private void onClickLogin(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                       loginRegisterActivity.goToMainActivity();
                } else {

                }
            }
        });
    }

    private void onClickSignup() {

    }

    private boolean check(String email, String pass) {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass);
    }

    public void init(View view) {
        btDN = view.findViewById(R.id.btDN);
        tvDK = view.findViewById(R.id.tvDKNgay);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPass = view.findViewById(R.id.edtPass);
    }

}
