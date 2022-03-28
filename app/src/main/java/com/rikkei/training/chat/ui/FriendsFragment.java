package com.rikkei.training.chat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.adapter.AdapterSearchUser;
import com.rikkei.training.chat.adapter.ViewPagerFriendsAdapter;
import com.rikkei.training.chat.modle.StatusFriends;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FriendsFragment extends Fragment {
    private MainActivity mainActivity;
    private View view;
    public List<User> userList;
    List<StatusFriends> statusFriendsList;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerFriendsAdapter friendsAdapter;
    private EditText edSearchFriends;
    private View view2;
    private TextView tvFriends;
    private RecyclerView rcvDataSearchFriends;
    private AdapterSearchUser adapterSearchUser;
    private TextView tvNoFindSearch;
    private ImageView imgNoFindSearch;
    private TextView tvCancel;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.friends_fragment, container, false);
        init();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
//        Handler handler=new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getData();
//            }
//        },2000);
        tvNoFindSearch.setVisibility(View.GONE);
        imgNoFindSearch.setVisibility(View.GONE);
        getData();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(FriendFriendsFragment.newInstance());
        fragmentList.add(AllFriendsFragment.newInstance());
        fragmentList.add(ReqFriendsFragment.newInstance());
        friendsAdapter = new ViewPagerFriendsAdapter(mainActivity, fragmentList);
        viewPager2.setAdapter(friendsAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(requireContext().getString(R.string.FRIENDS));
                    break;
                case 1:
                    tab.setText(getResources().getString(R.string.AllFriends));
                    break;
                case 2:
                    tab.setText(getResources().getString(R.string.ReqFriends));
                    break;
            }
        }).attach();

        return view;
    }

    public void getData() {
        userList = new ArrayList<>();
        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                for (DataSnapshot data : dataSnapshotIterable) {
                    User user1 = data.getValue(User.class);
                    userList.add(user1);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                rcvDataSearchFriends.setLayoutManager(layoutManager);
                search();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void search() {
        edSearchFriends.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                view2.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                viewPager2.setVisibility(View.GONE);
                tvFriends.setVisibility(View.VISIBLE);
                rcvDataSearchFriends.setVisibility(View.VISIBLE);
                tvNoFindSearch.setVisibility(View.GONE);
                imgNoFindSearch.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                view2.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                viewPager2.setVisibility(View.GONE);
                rcvDataSearchFriends.setVisibility(View.VISIBLE);
                adapterSearchUser = new AdapterSearchUser(sortUser(userList), mainActivity, tvFriends, tvNoFindSearch, imgNoFindSearch, edSearchFriends);
                rcvDataSearchFriends.setAdapter(adapterSearchUser);
                adapterSearchUser.getFilter().filter(edSearchFriends.getText().toString());
                edSearchFriends.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, R.drawable.cancel, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(edSearchFriends.getText().toString().trim())) {
                    view2.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager2.setVisibility(View.VISIBLE);
                    tvFriends.setVisibility(View.GONE);
                    rcvDataSearchFriends.setVisibility(View.GONE);
                    tvNoFindSearch.setVisibility(View.GONE);
                    imgNoFindSearch.setVisibility(View.GONE);
                    edSearchFriends.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
                } else {
                    view2.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    viewPager2.setVisibility(View.GONE);
                    //tvFriends.setVisibility(View.VISIBLE);
                    rcvDataSearchFriends.setVisibility(View.VISIBLE);
                    adapterSearchUser.getFilter().filter(edSearchFriends.getText().toString());
                    edSearchFriends.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, R.drawable.cancel, 0);
                }
            }
        });
    }

    public List<User> sortUser(List<User> userList) {
        List<User> users = new ArrayList<>();
        Comparator<User> comparator = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return getName(o1.getFullName()).compareTo(getName(o2.getFullName()));
            }
        };
        Collections.sort(userList, comparator);
        for (User u : userList) {
            if (!u.getId().equals(user.getUid()))
                users.add(u);
        }
        return users;
    }

    public String getName(String fullName) {
        String[] tg = fullName.split(" ");
        return tg[tg.length - 1];
    }

    public void init() {
        tabLayout = view.findViewById(R.id.tabLayoutFriends);
        viewPager2 = view.findViewById(R.id.viewPagerFriends);
        mainActivity = (MainActivity) getActivity();
        edSearchFriends = view.findViewById(R.id.edSearch);
        view2 = view.findViewById(R.id.view);
        tvFriends = view.findViewById(R.id.tvFriendsSearch);
        rcvDataSearchFriends = view.findViewById(R.id.rcvDataSearchFriends);
        tvNoFindSearch = view.findViewById(R.id.tvNoFindSearch);
        imgNoFindSearch = view.findViewById(R.id.imgNoFindSearch);
    }
}
