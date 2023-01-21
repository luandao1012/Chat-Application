package com.example.chatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.activities.ChatActivity;
import com.example.chatapplication.entities.User;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    Context mContext;
    List<User> users;

    public ContactAdapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (users.size() == 0) {
            return;
        }

        String ID = users.get(position).getID();
        Glide.with(mContext).load(users.get(position).getImage()).error(R.drawable.avatar_img).into(holder.imgContact);
        holder.txtContact.setText(users.get(position).getName());

        if (users.get(position).getStatus().equals("Online")) {
            holder.txtStatus.setText("Online");
        } else {
            holder.txtStatus.setText("Offline");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", ID);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
//        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgContact;
        TextView txtContact, txtStatus;
        ImageView imgInfo, imgDelete;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imgContact);
            txtContact = itemView.findViewById(R.id.txtContact);
            imgInfo = itemView.findViewById(R.id.imgInfo);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
