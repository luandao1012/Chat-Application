package com.example.chatapplication.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    Button btnDone;
    ImageView imgBack;
    ShapeableImageView imgAvatar;
    EditText edtName, edtUsername, edtBio, edtTag;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReferenceUser;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();
        loadData();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void init() {
        imgBack = findViewById(R.id.imgBackEditProfile);
        imgAvatar = findViewById(R.id.imgEditProfile);
        edtName = findViewById(R.id.edtNameEditProfile);
        edtUsername = findViewById(R.id.edtUsernameEditProfile);
        edtBio = findViewById(R.id.edtBioEditProfile);
        edtTag = findViewById(R.id.edtTagEditProfile);
        btnDone = findViewById(R.id.btnDoneEditProfile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("avatar");

    }

    private void loadData() {
        databaseReferenceUser.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getBaseContext()).load(user.getImage()).into(imgAvatar);
                edtName.setText(user.getName());
                edtUsername.setText(user.getUsername());
                edtBio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Update Profile")
                .setMessage("Are you sure ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadInfo();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void uploadInfo() {
        String name = String.valueOf(edtName.getText());
        String username = String.valueOf(edtUsername.getText());
        String bio = String.valueOf(edtBio.getText());
        String tag = String.valueOf(edtTag.getText());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("username", username);
        hashMap.put("bio", bio);
        hashMap.put("tag", tag);
        databaseReferenceUser.child(firebaseUser.getUid()).updateChildren(hashMap);

        if (uriImage != null) {
            storageReference.child(firebaseUser.getUid()).putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.child(firebaseUser.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            hashMap.put("image", task.getResult().toString());
                            databaseReferenceUser.child(firebaseUser.getUid()).updateChildren(hashMap);
                        }
                    });
                }
            });
            Toast.makeText(EditProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uriImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uriImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAvatar.setImageBitmap(bitmap);
            } catch (Exception e) {

            }
        }
    }
}