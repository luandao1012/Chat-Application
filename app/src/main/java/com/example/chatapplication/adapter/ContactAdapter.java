package com.example.chatapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
        if (users.size() == 0){
            return;
        }
//        public void setContact(User user) {
//            // load image
//            class LoadImage extends AsyncTask<String, Void, Bitmap> {
//                Bitmap bitmap = null;
//
//                @Override
//                protected Bitmap doInBackground(String... strings) {
//                    try {
//                        URL url = new URL(strings[0]);
//                        InputStream inputStream = url.openStream();
//                        bitmap = BitmapFactory.decodeStream(inputStream);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return bitmap;
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap bitmap) {
//                    super.onPostExecute(bitmap);
//                    imgContact.setImageBitmap(bitmap);
//                }
//            }
//
//            new LoadImage().execute(user.getImage());
            Glide.with(mContext).load(users.get(position).getImage()).error(R.drawable.avatar_img).into(holder.imgContact);
            holder.txtContact.setText(users.get(position).getName());
//        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgContact;
        TextView txtContact;
        ImageView imgChat, imgInfo, imgDelete;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imgContact);
            txtContact = itemView.findViewById(R.id.txtContact);
            imgChat = itemView.findViewById(R.id.imgChat);
            imgInfo = itemView.findViewById(R.id.imgInfo);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
