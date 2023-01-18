package com.example.chatapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.entities.User;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView txtName, txtUserName;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    LinearLayout layoutBackHome, layoutBackSetting;
    Button btnLogout, btnEditProfile;
    ShapeableImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        setView();
    }

    private void init(){
        txtName = findViewById(R.id.txtNameProfile);
        txtUserName = findViewById(R.id.txtUsernameProfile);
        layoutBackHome = findViewById(R.id.profileBackHome);
        layoutBackSetting = findViewById(R.id.profileBackSetting);
        btnLogout = findViewById(R.id.btnLogout);
        imgAvatar = findViewById(R.id.imgProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        layoutBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
    }

    private void setView() {
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                assert user != null;
                txtUserName.setText(user.getUsername());
                Glide.with(getBaseContext()).load(user.getImage()).into(imgAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}