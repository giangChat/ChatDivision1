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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rikkei.training.chat.R;
import com.rikkei.training.chat.adapter.AdapterSearchUser;
import com.rikkei.training.chat.adapter.ViewPagerFriendsAdapter;
import com.rikkei.training.chat.modle.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendsFragment extends Fragment {
    private MainActivity mainActivity;
    private View view;
    private List<User> userList;
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
        getData();
        tvNoFindSearch.setVisibility(View.GONE);
        imgNoFindSearch.setVisibility(View.GONE);
        friendsAdapter = new ViewPagerFriendsAdapter(mainActivity);
        viewPager2.setAdapter(friendsAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(getContext().getString(R.string.FRIENDS));
                        break;
                    case 1:
                        tab.setText(getContext().getString(R.string.AllFriends));
                        break;
                    case 2:
                        tab.setText(getContext().getString(R.string.ReqFriends));
                        break;
                }
            }
        }).attach();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
        rcvDataSearchFriends.setLayoutManager(layoutManager);
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
                //tvFriends.setVisibility(View.VISIBLE);
                rcvDataSearchFriends.setVisibility(View.VISIBLE);
                adapterSearchUser = new AdapterSearchUser(sortUser(userList), mainActivity, tvFriends, tvNoFindSearch, imgNoFindSearch, edSearchFriends);
                rcvDataSearchFriends.setAdapter(adapterSearchUser);
                adapterSearchUser.getFilter().filter(edSearchFriends.getText().toString());
                edSearchFriends.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, R.drawable.cancel, 0);
                edSearchFriends.setWidth(edSearchFriends.getWidth() - 12 - tvCancel.getWidth());
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edSearchFriends.setText("");
                        tvCancel.setVisibility(View.GONE);
                    }
                });
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
                    edSearchFriends.setWidth(edSearchFriends.getWidth() + 12 + tvCancel.getWidth());
                    tvCancel.setVisibility(View.GONE);
                } else {
                    view2.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    viewPager2.setVisibility(View.GONE);
                    //tvFriends.setVisibility(View.VISIBLE);
                    rcvDataSearchFriends.setVisibility(View.VISIBLE);
                    adapterSearchUser.getFilter().filter(edSearchFriends.getText().toString());
                    edSearchFriends.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, R.drawable.cancel, 0);
                    edSearchFriends.setWidth(edSearchFriends.getWidth() - 12 - tvCancel.getWidth());
                    tvCancel.setVisibility(View.VISIBLE);
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edSearchFriends.setText("");
                            tvCancel.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        return view;
    }

    public void getData() {
        //TODO Remove
        userList = new ArrayList<>();
        User user = new User("Vu Giang", "", "", "", "vugiang@gmail.com", "123");
        User user1 = new User("Vu Anh", "", "", "", "vuanh@gmail.com", "123");
        User user2 = new User("Vu An", "", "", "", "vuan@gmail.com", "123");
        User user3 = new User("Vu Duc", "", "", "", "vuduc@gmail.com", "123");
        User user4 = new User("Vu Hoa", "", "", "", "vuhoa@gmail.com", "123");
        User user5 = new User("Vu Ha", "", "", "", "vuha@gmail.com", "123");
        User user6 = new User("Vu Cuong", "", "", "", "vucuong@gmail.com", "123");
        User user7 = new User("Vu Tung", "", "", "", "vutung@gmail.com", "123");
        User user8 = new User("Vu Manh", "", "", "", "vumanh@gmail.com", "123");
        User user9 = new User("Vu Kien", "", "", "", "vukien@gmail.com", "123");
        User user10 = new User("Vu Tuan", "", "", "", "vutuan@gmail.com", "123");
        userList.add(user);
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        userList.add(user6);
        userList.add(user7);
        userList.add(user8);
        userList.add(user9);
        userList.add(user10);
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
        tvCancel = view.findViewById(R.id.tvCancel);
    }
}
