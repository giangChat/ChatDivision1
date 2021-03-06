package com.rikkei.training.chat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rikkei.training.chat.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    View viewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        //sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
        //editor = sharedPreferences.edit();
        //changeLanguage(sharedPreferences.getString("lang", "en"));
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_friends:
                    setFragment(FriendsFragment.newInstance(), false);
                    return true;
                case R.id.navigation_message:
                    setFragment(new FragmentMessage(), false);
                    return true;
                case R.id.navigation_account:
                    setFragment(new ProfileFragment(), false);
                    return true;
                default:
                    return true;
            }
        });
        setFragment(new FragmentMessage(), false);
    }

    public void changeVisibleBottomSheet(boolean isVisible) {
        if (isVisible) {
            viewBottom.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            viewBottom.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void setFragment(Fragment fragment, boolean isBackTack) {
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment);
        if (isBackTack) {
            transaction.addToBackStack(fragment.toString());
        }
        transaction.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewBottom = findViewById(R.id.viewBottom);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE) ;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken() , 0);
    }
    public void checkKeyBord(View view){
        view = findViewById(R.id.mainActivity);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

            }
        });
    }

    public void setEditor(String language) {
        editor.putString("lang", language);
        editor.commit();
    }

    public void changeLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        createConfigurationContext(configuration);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}