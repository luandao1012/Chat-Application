package com.example.chatapplication.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.chatapplication.fragment.CommunityFragment;
import com.example.chatapplication.fragment.ContactFragment;
import com.example.chatapplication.fragment.InboxFragment;

public class TaskbarHomeAdapter extends FragmentStatePagerAdapter {

    public TaskbarHomeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new InboxFragment();
            case 1:
                return new CommunityFragment();
            case 2:
                return new ContactFragment();
            default:
                return new InboxFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Inbox";
                break;
            case 1:
                title = "Community";
                break;
            case 2:
                title = "Contact";
                break;
        }
        return title;
    }
}
