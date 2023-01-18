package com.example.chatapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ContactAdapter;
import com.example.chatapplication.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactFragment extends Fragment {
    RecyclerView rvContact;
    List<User> users;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ContactAdapter contactAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvContact = view.findViewById(R.id.recycler_view_contact);
        rvContact.setLayoutManager(new LinearLayoutManager(getContext()));

        users = new ArrayList<>();
        contactAdapter = new ContactAdapter(getContext(), users);

        getListUser();
        rvContact.setAdapter(contactAdapter);
    }

    private void getListUser() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                if (!user.getEmail().equals(firebaseUser.getEmail())) {
                    users.add(user);
                    contactAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                for(int i=0; i<users.size(); i++){
                    if(user.getUsername().equals(users.get(i).getUsername())){
                        users.set(i, user);
                        contactAdapter.notifyDataSetChanged();
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
}