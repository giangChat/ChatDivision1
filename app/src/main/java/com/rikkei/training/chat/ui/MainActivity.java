package com.rikkei.training.chat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.ui.FriendsFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_friends:
                        getFragment(FriendsFragment.newInstance());
                        FriendsFragment.newInstance();
                        return true;
                    case R.id.navigation_message:
                        return true;
                    case R.id.navigation_account:
                        return true;
                    default:
                        return true;
                }
            }
        });
        // edittext.addTextChangeListener( new textwatch)...
    }
    public void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }
    public void init(){
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
    }
}