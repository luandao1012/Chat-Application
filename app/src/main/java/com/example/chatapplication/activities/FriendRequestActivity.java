package com.example.chatapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.FriendRequestAdapter;
import com.example.chatapplication.entities.User;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestActivity extends AppCompatActivity {
    RecyclerView rvFriendRequest;
    List<User> users;
    LinearLayout layoutBackHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        rvFriendRequest = findViewById(R.id.recycler_view_friend_request);
        layoutBackHome = findViewById(R.id.friendRequestBackHome);
        rvFriendRequest.setLayoutManager(new LinearLayoutManager(getBaseContext()));


        users = new ArrayList<>();

        FriendRequestAdapter friendRequestAdapter = new FriendRequestAdapter(users, getBaseContext());
        rvFriendRequest.setAdapter(friendRequestAdapter);

        layoutBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}