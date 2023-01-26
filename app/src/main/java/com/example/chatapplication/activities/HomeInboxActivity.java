package com.example.chatapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.adapter.TaskbarHomeAdapter;
import com.example.chatapplication.entities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeInboxActivity extends AppCompatActivity {
    ImageView imgProfileHome;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton FABAddCommunity, FBAAddFriend, FABOption;
    Boolean showFAB = false;
    Animation animRotateOpenFAB, animRotateCloseFAB;
    TextView txtFABAddFriend, txtFABCreateCommunity;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_inbox);
        init();
        setView();
    }


    public void init() {
        imgProfileHome = findViewById(R.id.imgProfileHome);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        FABOption = findViewById(R.id.FABOption);
        FABAddCommunity = findViewById(R.id.FABAddCommunity);
        FBAAddFriend = findViewById(R.id.FABAddFriend);
        txtFABAddFriend = findViewById(R.id.txtFABAddFriend);
        txtFABCreateCommunity = findViewById(R.id.txtFABCreateCommunity);
        animRotateOpenFAB = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_fab_open);
        animRotateCloseFAB = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_fab_close);

        FABAddCommunity.setVisibility(View.GONE);
        FBAAddFriend.setVisibility(View.GONE);
        txtFABAddFriend.setVisibility(View.GONE);
        txtFABCreateCommunity.setVisibility(View.GONE);

        TaskbarHomeAdapter taskbarHomeAdapter = new TaskbarHomeAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(taskbarHomeAdapter);
        tabLayout.setupWithViewPager(viewPager);

        imgProfileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeInboxActivity.this, ProfileActivity.class));
            }
        });

        FABOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!showFAB) {
                    FABOption.startAnimation(animRotateOpenFAB);
                    FABAddCommunity.setVisibility(View.VISIBLE);
                    FBAAddFriend.setVisibility(View.VISIBLE);
                    txtFABAddFriend.setVisibility(View.VISIBLE);
                    txtFABCreateCommunity.setVisibility(View.VISIBLE);
                    showFAB = true;
                } else {
                    FABOption.startAnimation(animRotateCloseFAB);
                    FABAddCommunity.setVisibility(View.GONE);
                    FBAAddFriend.setVisibility(View.GONE);
                    txtFABAddFriend.setVisibility(View.GONE);
                    txtFABCreateCommunity.setVisibility(View.GONE);
                    showFAB = false;
                }
            }
        });

        FBAAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeInboxActivity.this, FriendRequestActivity.class));
            }
        });
    }
    public void setView(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                Glide.with(getBaseContext()).load(user.getImage()).into(imgProfileHome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void checkOnlineStatus(String status){
        HashMap<String, Object> mapStatus = new HashMap<>();
        mapStatus.put("status", status);
        databaseReference.child(firebaseUser.getUid()).updateChildren(mapStatus);
    }

    @Override
    protected void onStart() {
        checkOnlineStatus("Online");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        checkOnlineStatus("Offline");
        super.onDestroy();
    }
}