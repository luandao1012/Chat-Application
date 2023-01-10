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
import com.example.chatapplication.entities.User;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {
    private List<User> users;
    private Context mContext;

    public FriendRequestAdapter(List<User> users, Context mContext) {
        this.users = users;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        if(users.size() == 0) {
            return;
        }
        Glide.with(mContext).load(users.get(position).getImage()).error(R.drawable.avatar_img).into(holder.imgFriend);
        holder.txtUsername.setText(users.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class FriendRequestViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView imgFriend;
        TextView txtUsername;
        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFriend = itemView.findViewById(R.id.imgFriendRequest);
            txtUsername = itemView.findViewById(R.id.txtFriendRequest);
        }
    }
}
