package com.example.chatapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.TaskbarHomeAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomeInboxActivity extends AppCompatActivity {
    ImageView imgProfileHome;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_inbox);

        imgProfileHome = findViewById(R.id.imgProfileHome);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        TaskbarHomeAdapter taskbarHomeAdapter = new TaskbarHomeAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(taskbarHomeAdapter);
        tabLayout.setupWithViewPager(viewPager);

        imgProfileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeInboxActivity.this, ProfileActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}