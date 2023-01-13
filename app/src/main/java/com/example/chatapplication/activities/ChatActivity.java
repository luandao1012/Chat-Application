package com.example.chatapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapplication.GetRoomCallback;
import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ChatAdapter;
import com.example.chatapplication.entities.Message;
import com.example.chatapplication.entities.RoomInbox;
import com.example.chatapplication.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageView imgAvatar, imgBack, imgSend;
    TextView txtUsername, txtStatus;
    EditText edtMessage;
    RecyclerView rvChat;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databaseReferenceUser, checkSeenMessage;
    boolean roomAvailable = false;
    List<Message> messageList;
    String imgURL, IDRoom;
    ChatAdapter chatAdapter;
    ValueEventListener seenListener;
    RoomInbox room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        setView(new GetRoomCallback() {
            @Override
            public void getCallback(RoomInbox roomInbox) {
                room = roomInbox;

                ChatAdapter chatAdapter = new ChatAdapter(getBaseContext(), messageList, room.getImage());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
                linearLayoutManager.setStackFromEnd(true);
                rvChat.setHasFixedSize(true);
                rvChat.setLayoutManager(linearLayoutManager);

                rvChat.setAdapter(chatAdapter);

                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                imgSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = edtMessage.getText().toString().trim();
                        if (TextUtils.isEmpty(message)) {
                            Toast.makeText(ChatActivity.this, "Cannot send the empty message...", Toast.LENGTH_SHORT).show();
                        } else {
                            sendMessage(message, room.getID());
                        }
                    }
                });

        readMessage(room.getID());
        seenMessage(room.getID());
            }
        });

    }


    private void init() {
        imgAvatar = findViewById(R.id.imgAvatarChat);
        imgSend = findViewById(R.id.imgSendMessage);
        imgBack = findViewById(R.id.imgBackChat);
        txtUsername = findViewById(R.id.txtUsernameChat);
        txtStatus = findViewById(R.id.txtStatusChat);
        edtMessage = findViewById(R.id.edtMessage);
        rvChat = findViewById(R.id.recycler_view_chat);
        messageList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("rooms");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
    }

    private void setView(GetRoomCallback getRoomCallback) {
        Intent intent = getIntent();
        String ID = intent.getStringExtra("uid");
        String currentUser = firebaseUser.getUid();

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

                RoomInbox roomInbox;
                if (roomAvailable) {
                    roomInbox = dataSnapshot.getValue(RoomInbox.class);
                    getRoomCallback.getCallback(roomInbox);
                    databaseReferenceUser.child(ID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            assert user != null;
                            txtUsername.setText(user.getName());
                            txtStatus.setText(user.getStatus());
                            Glide.with(getBaseContext()).load(user.getImage()).into(imgAvatar);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    roomInbox = new RoomInbox();
                    roomInbox.setParticipants(participants);
                    roomInbox.setID(ID + "-" + firebaseUser.getUid());
                    getRoomCallback.getCallback(roomInbox);
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

    private void sendMessage(String message, String roomId) {
        Message oMessage = new Message();
        oMessage.setMessage(message);
        oMessage.setSender(firebaseUser.getUid());
        oMessage.setType("text");
        oMessage.setSeen(false);
        oMessage.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        oMessage.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        Query lastQuery = databaseReference.child(roomId).child("messages").orderByKey().limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        int id = Integer.parseInt(data.getKey());
                        oMessage.setId(id);
                        databaseReference.child(roomId).child("messages").child(String.valueOf(id + 1)).setValue(oMessage);
                    }

                } else {
                    oMessage.setId(0);
                    databaseReference.child(roomId).child("messages").child("0").setValue(oMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        edtMessage.setText("");
    }

    private void readMessage(String roomID) {
        messageList = new ArrayList<>();

        databaseReference.child(roomID).child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                messageList.add(message);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenMessage(String roomID) {
        checkSeenMessage = databaseReference.child(roomID).child("message");
        seenListener = checkSeenMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("isSeen", true);

                    databaseReference.child(roomID).child("message").child(String.valueOf(message.getId())).updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkSeenMessage.removeEventListener(seenListener);
    }
}
