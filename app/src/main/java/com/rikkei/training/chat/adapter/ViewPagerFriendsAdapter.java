package com.rikkei.training.chat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rikkei.training.chat.ui.AllFriendsFragment;
import com.rikkei.training.chat.ui.FriendFriendsFragment;
import com.rikkei.training.chat.ui.ReqFriendsFragment;

public class ViewPagerFriendsAdapter extends FragmentStateAdapter {

    public ViewPagerFriendsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FriendFriendsFragment();
            case 1:
                return new AllFriendsFragment();
            case 2:
                return new ReqFriendsFragment();
            default:
                return new FriendFriendsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
