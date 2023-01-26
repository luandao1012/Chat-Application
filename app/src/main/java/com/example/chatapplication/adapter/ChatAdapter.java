package com.example.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.entities.Message;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final int MSG_SEND = 0;
    private static final int MSG_RECEIVER = 1;
    Context mContext;
    List<Message> messageList;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String imgUrl;

    public ChatAdapter(Context mContext, List<Message> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_SEND) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_sender, parent, false);
            return new ChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_receiver, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        holder.txtMessage.setText(messageList.get(position).getMessage());
        holder.txtTimeMessage.setText(messageList.get(position).getTime().substring(0, 5));

        try {
            Glide.with(mContext).load(imgUrl).into(holder.imgAvatarReceiver);
        } catch (Exception e) {

        }
        try {
            if (messageList.get(position).getIsSeen() == 1) {
                holder.imgIsSeen.setVisibility(View.VISIBLE);
            } else {
                holder.imgIsSeen.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messageList.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_SEND;
        } else {
            return MSG_RECEIVER;
        }
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgAvatarReceiver;
        TextView txtMessage, txtTimeMessage;
        ImageView imgIsSeen;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatarReceiver = itemView.findViewById(R.id.imgAvatarChatReceiver);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTimeMessage = itemView.findViewById(R.id.txtTimeMessage);
            imgIsSeen = itemView.findViewById(R.id.imgIsSeen);
        }
    }
}
