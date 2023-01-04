package com.example.chatapplication.fragment;

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

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    RecyclerView rvContact;
    List<User> users;
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
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        users.add(new User("luan", "Luan", "daovanluan201@gmail.com", "12345", "123", "https://res.cloudinary.com/dp4fkm6ke/image/upload/v1668440767/IMG_20221018_201849_c7numh.jpg", null));
        ContactAdapter contactAdapter = new ContactAdapter(getContext(), users);
        rvContact.setAdapter(contactAdapter);

        contactAdapter.notifyDataSetChanged();
    }
}