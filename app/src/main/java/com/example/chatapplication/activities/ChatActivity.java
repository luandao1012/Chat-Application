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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    ChatAdapter chatAdapter;
    ValueEventListener seenListener;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        setView(new GetRoomCallback() {
            @Override
            public void getCallback(RoomInbox roomInbox) {

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
                            sendMessage(message, roomInbox.getID());
                        }
                    }
                });

                readMessage(roomInbox.getID());
                seenMessage(roomInbox.getID());

//                if (rvChat.getAdapter().getItemCount() > 0) {
//                    rvChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                        @Override
//                        public void onLayoutChange(View v,
//                                                   int left, int top, int right, int bottom,
//                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                            if (bottom < oldBottom) {
//                                rvChat.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        rvChat.smoothScrollToPosition(rvChat.getAdapter().getItemCount() - 1);
//                                    }
//                                }, 100);
//                            }
//                        }
//                    });
//                }

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

        Intent intent = getIntent();
        ID = intent.getStringExtra("uid");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getBaseContext(), messageList);

        rvChat.setLayoutManager(linearLayoutManager);
        rvChat.setAdapter(chatAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("rooms");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
    }

    private void setView(GetRoomCallback getRoomCallback) {

        List<String> participants = new ArrayList<>();
        participants.add(ID);
        participants.add(firebaseUser.getUid());
        HashMap<String, String> image = new HashMap<>();
        HashMap<String, String> name = new HashMap<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    setViewChat(ID, roomInbox);
                } else {
                    roomInbox = new RoomInbox();
                    roomInbox.setParticipants(participants);
                    roomInbox.setID(ID + "-" + firebaseUser.getUid());

                    databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                if (dataSnapshot.getKey().equals(ID)){
                                    User user = dataSnapshot.getValue(User.class);
                                    image.put(ID, user.getImage());
                                    name.put(ID, user.getName());
                                    txtStatus.setText(user.getStatus());
                                }
                                if (dataSnapshot.getKey().equals(firebaseUser.getUid())){
                                    User user = dataSnapshot.getValue(User.class);
                                    image.put(firebaseUser.getUid(), user.getImage());
                                    name.put(firebaseUser.getUid(), user.getName());
                                }
                            }
                            roomInbox.setImage(image);
                            roomInbox.setName(name);
                            databaseReference.child(ID + "-" + firebaseUser.getUid()).setValue(roomInbox);
                            setViewChat(ID,roomInbox);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
                getRoomCallback.getCallback(roomInbox);
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
        oMessage.setIsSeen(0);
        oMessage.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        oMessage.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        updateRoom(roomId, oMessage);

        databaseReference.child(roomId).child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (rvChat.getAdapter().getItemCount() == 0) {
                    oMessage.setId(0);
                    snapshot.getRef().child("0").setValue(oMessage);
                } else {
                    oMessage.setId(rvChat.getAdapter().getItemCount());
                    snapshot.getRef().child(String.valueOf(oMessage.getId())).setValue(oMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edtMessage.setText("");

    }
    private void setViewChat(String id, RoomInbox roomInbox){
        Glide.with(getBaseContext()).load(roomInbox.getImageFromID(id)).into(imgAvatar);
        txtUsername.setText(roomInbox.getNameFromID(id));
        chatAdapter.setImgUrl(roomInbox.getImageFromID(id));
    }

    private void updateRoom(String roomID, Message message) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lastMessage", message.getMessage());
        hashMap.put("timeLastMessage", message.getTime());
        hashMap.put("dateLastMessage", message.getDate());
        hashMap.put("senderLastMessage", message.getSender());

        databaseReference.child(roomID).updateChildren(hashMap);
    }

    private void readMessage(String roomID) {

        databaseReference.child(roomID).child("messages").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                messageList.add(message);
                chatAdapter.notifyItemInserted(messageList.size());
                rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Message message = snapshot.getValue(Message.class);
                for (int i = messageList.size() - 1; i >= 0; i--) {
                    if (messageList.get(i).getId() == message.getId()) {
                        messageList.set(i, message);

                        chatAdapter.notifyDataSetChanged();
                    }
                }
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

        checkSeenMessage = databaseReference.child(roomID).child("messages");
        seenListener = checkSeenMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (!message.getSender().equals(firebaseUser.getUid())) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", 1);
                        snapshot.getRef().child(String.valueOf(message.getId())).updateChildren(hashMap);
                    }
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
