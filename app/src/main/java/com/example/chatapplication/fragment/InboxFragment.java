package com.example.chatapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.InboxAdapter;
import com.example.chatapplication.entities.RoomInbox;
import com.example.chatapplication.notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();
                updateToken(token);
            }
        });
    }

    private void init(View view) {

        roomInboxList = new ArrayList<>();

        rvInbox = view.findViewById(R.id.recycler_view_inbox);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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
                        sortRoom();
                        inboxAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomInbox roomInbox = snapshot.getValue(RoomInbox.class);
//                for (RoomInbox room : roomInboxList) {
//                    if (room.getID().equals(roomInbox.getID())) {
//                        if (room.getMessages().size() < roomInbox.getMessages().size()) {
//                            roomInboxList.remove(room);
//                            roomInboxList.add(0, roomInbox);
//                            break;
//                        }
//                    }
//                }
                for (int i = 0; i < roomInboxList.size(); i++) {
                    if (roomInbox.getID().equals(roomInboxList.get(i).getID())) {
                        roomInboxList.set(i, roomInbox);
                        break;
                    }
                }
                sortRoom();
                inboxAdapter.notifyDataSetChanged();
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

    private void updateToken(String token) {
        DatabaseReference databaseReferenceToken = FirebaseDatabase.getInstance().getReference("tokens");
        Token token1 = new Token(token);
        databaseReferenceToken.child(uid).setValue(token1);
    }

    private void sortRoom() {
        Collections.sort(roomInboxList, new Comparator<RoomInbox>() {
            @Override
            public int compare(RoomInbox o1, RoomInbox o2) {
                String date1 = o1.getDateLastMessage() + " " + o1.getTimeLastMessage();
                String date2 = o2.getDateLastMessage() + " " + o2.getTimeLastMessage();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                try {
                    Date dateo1 = simpleDateFormat.parse(date1);
                    Date dateo2 = simpleDateFormat.parse(date2);

                    return dateo2.compareTo(dateo1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}