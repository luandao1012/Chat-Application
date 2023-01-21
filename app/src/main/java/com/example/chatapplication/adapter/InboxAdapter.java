package com.example.chatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.activities.ChatActivity;
import com.example.chatapplication.entities.RoomInbox;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {
    Context mContext;
    List<RoomInbox> roomInboxes;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public InboxAdapter(Context mContext, List<RoomInbox> roomInboxes) {
        this.mContext = mContext;
        this.roomInboxes = roomInboxes;
    }


    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_inbox, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        if (roomInboxes.size() == 0) {
            return;
        }

        RoomInbox roomInbox = roomInboxes.get(position);
        List<String> users = roomInbox.getParticipants();

        String ID = null;
        for (String i : users) {
            if (!i.equals(firebaseUser.getUid())) {
                ID = i;
            }
        }

        holder.txtInbox.setText(roomInboxes.get(position).getNameFromID(ID));

        if (roomInbox.getSenderLastMessage().equals(firebaseUser.getUid())) {
            holder.txtLastMessage.setText("You: " + roomInboxes.get(position).getLastMessage());
        } else {
//            if (roomInbox.getIsSeenLastMessage() == 0) {
//                holder.txtLastMessage.setTextColor(Color.parseColor("#FEFCFC"));
//                holder.txtLastMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//            }
            holder.txtLastMessage.setText(roomInboxes.get(position).getLastMessage());
        }
        holder.txtTimeInbox.setText(roomInboxes.get(position).getTimeLastMessage());
        Glide.with(mContext).load(roomInboxes.get(position).getImageFromID(ID)).into(holder.imgInbox);

        String finalID = ID;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", finalID);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomInboxes.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgInbox;
        TextView txtInbox, txtLastMessage, txtTimeInbox;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);

            imgInbox = itemView.findViewById(R.id.imgInbox);
            txtInbox = itemView.findViewById(R.id.txtInbox);
            txtLastMessage = itemView.findViewById(R.id.txtLastMessageInbox);
            txtTimeInbox = itemView.findViewById(R.id.txtTimeInbox);
        }
    }
}
