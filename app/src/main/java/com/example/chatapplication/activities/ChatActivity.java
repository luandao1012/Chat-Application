package com.example.chatapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.entities.RoomInbox;
import com.example.chatapplication.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageView imgAvatar, imgBack, imgSend;
    TextView txtUsername, txtStatus;
    EditText edtMessage;
    RecyclerView rvChat;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databaseReferenceUser;
    boolean roomAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        setView();
    }

    private void init() {
        imgAvatar = findViewById(R.id.imgAvatarChat);
        imgBack = findViewById(R.id.imgSendMessage);
        imgSend = findViewById(R.id.imgBackChat);
        txtUsername = findViewById(R.id.txtUsernameChat);
        txtStatus = findViewById(R.id.txtStatusChat);
        edtMessage = findViewById(R.id.edtMessage);
        rvChat = findViewById(R.id.recycler_view_chat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("rooms");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
    }

    private void setView() {
        Intent intent = getIntent();
        String ID = intent.getStringExtra("uid");

        List<String> participants = new ArrayList<>();
        participants.add(ID);
        participants.add(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot dataSnapshot = null;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (postSnapshot.getKey().contains(firebaseUser.getUid()) && postSnapshot.getKey().contains(ID)) {
                        roomAvailable = true;
                        dataSnapshot = postSnapshot;
                        break;
                    }
                }

                if (roomAvailable) {
                    RoomInbox roomInbox = dataSnapshot.getValue(RoomInbox.class);
                    Log.d("Room", roomInbox.getID());
                    txtUsername.setText(roomInbox.getName());
                    Glide.with(getBaseContext()).load(roomInbox.getImage()).into(imgAvatar);

                    databaseReferenceUser.child(ID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            assert user != null;
                            txtStatus.setText(user.getStatus());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    RoomInbox roomInbox = new RoomInbox();
                    roomInbox.setParticipants(participants);
                    roomInbox.setID(ID + "-" + firebaseUser.getUid());

                    databaseReferenceUser.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            roomInbox.setImage(user.getImage());
                            roomInbox.setName(user.getName());
                            databaseReference.child(ID + "-" + firebaseUser.getUid()).setValue(roomInbox);
                            txtStatus.setText(user.getStatus());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    txtUsername.setText(roomInbox.getName());
                    Glide.with(getBaseContext()).load(roomInbox.getImage()).into(imgAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
