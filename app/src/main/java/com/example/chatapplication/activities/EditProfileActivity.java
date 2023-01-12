package com.example.chatapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.chatapplication.R;
import com.google.android.material.imageview.ShapeableImageView;

public class EditProfileActivity extends AppCompatActivity {
    ImageView imgBack;
    ShapeableImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imgBack = findViewById(R.id.imgBackEditProfile);
        imgAvatar = findViewById(R.id.imgEditProfile);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

}