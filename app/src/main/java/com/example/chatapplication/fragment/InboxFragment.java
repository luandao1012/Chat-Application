package com.example.chatapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.sax.RootElement;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.adapter.InboxAdapter;
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
import java.util.List;

public class InboxFragment extends Fragment {
    RecyclerView rvInbox;
    List<RoomInbox> roomInboxList;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReferenceRoom, databaseReferenceUser;
    InboxAdapter inboxAdapter;
    String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        getListRoom(uid);
    }

    private void init(View view) {

        roomInboxList = new ArrayList<>();

        rvInbox = view.findViewById(R.id.recycler_view_inbox);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
        rvInbox.setLayoutManager(linearLayoutManager);
        inboxAdapter = new InboxAdapter(getContext(), roomInboxList);
        rvInbox.setAdapter(inboxAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        databaseReferenceRoom = FirebaseDatabase.getInstance().getReference("rooms");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
    }

    private void getListRoom(String uid) {

        databaseReferenceRoom.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().contains(uid)) {

                    RoomInbox roomInbox = snapshot.getValue(RoomInbox.class);
                    if (roomInbox.getLastMessage() != null) {
                        roomInboxList.add(roomInbox);
                        inboxAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                RoomInbox roomInbox = snapshot.getValue(RoomInbox.class
//                roomInboxList.add(roomInbox);
//                Log.d("Room", String.valueOf(roomInboxList.size()));
//                inboxAdapter.notifyItemChanged(roomInboxList.size());
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

}