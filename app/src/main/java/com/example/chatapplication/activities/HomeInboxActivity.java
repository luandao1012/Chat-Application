package com.example.chatapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.TaskbarHomeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomeInboxActivity extends AppCompatActivity {
    ImageView imgProfileHome;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton FABAddCommunity, FBAAddFriend, FABOption;
    Boolean showFAB = false;
    Animation animRotateOpenFAB, animRotateCloseFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_inbox);
        init();

    }

    public void init() {
        imgProfileHome = findViewById(R.id.imgProfileHome);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        FABOption = findViewById(R.id.FABOption);
        FABAddCommunity = findViewById(R.id.FABAddCommunity);
        FBAAddFriend = findViewById(R.id.FABAddFriend);
        animRotateOpenFAB = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_fab_open);
        animRotateCloseFAB = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_fab_close);

        FABAddCommunity.setVisibility(View.GONE);
        FBAAddFriend.setVisibility(View.GONE);

        TaskbarHomeAdapter taskbarHomeAdapter = new TaskbarHomeAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(taskbarHomeAdapter);
        tabLayout.setupWithViewPager(viewPager);

        imgProfileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeInboxActivity.this, ProfileActivity.class));
                finish();
            }
        });

        FABOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!showFAB) {
                    FABOption.startAnimation(animRotateOpenFAB);
                    FABAddCommunity.setVisibility(View.VISIBLE);
                    FBAAddFriend.setVisibility(View.VISIBLE);
                    showFAB = true;
                } else {
                    FABOption.startAnimation(animRotateCloseFAB);
                    FABAddCommunity.setVisibility(View.GONE);
                    FBAAddFriend.setVisibility(View.GONE);
                    showFAB = false;
                }
            }
        });
    }

}