package com.example.chatapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.InboxAdapter;
import com.example.chatapplication.entities.RoomInbox;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {
    RecyclerView rvInbox;
    List<RoomInbox> roomInboxes;

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

        rvInbox = view.findViewById(R.id.recycler_view_inbox);
        rvInbox.setLayoutManager(new LinearLayoutManager(getContext()));

        roomInboxes = new ArrayList<>();

        InboxAdapter inboxAdapter = new InboxAdapter(getContext(), roomInboxes);
        rvInbox.setAdapter(inboxAdapter);

        inboxAdapter.notifyDataSetChanged();
    }
}