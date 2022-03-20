package com.rikkei.training.chat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.ui.FriendsFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        sharedPreferences=getSharedPreferences("language",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        changeLanguage(sharedPreferences.getString("lang","en"));
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_friends:
                        getFragment(FriendsFragment.newInstance());
                        return true;
                    case R.id.navigation_message:
                        return true;
                    case R.id.navigation_account:
                        getFragment(ProfileFragment.newInstance());
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    public void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}