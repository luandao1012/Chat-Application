package com.example.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.entities.RoomInbox;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder>{
    Context mContext;
    List<RoomInbox> roomInboxes;

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
        if(roomInboxes.size() == 0) {
            return;
        }
        holder.txtInbox.setText(roomInboxes.get(position).getName());
        holder.txtLastMessage.setText(roomInboxes.get(position).getLastMessage());
        holder.txtTimeInbox.setText(roomInboxes.get(position).getTimeLastMessage());
    }

    @Override
    public int getItemCount() {
        return roomInboxes.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder{

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
