package com.example.chatapplication.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.R;
import com.example.chatapplication.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    TextView txtLoginNow;
    EditText edtUsername, edtEmail, edtPhone;
    TextInputLayout edtPassword;
    Button btnRegister;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        Intent intentToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
        txtLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentToLogin);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username, email, phone, password;
                username = edtUsername.getText().toString();
                email = edtEmail.getText().toString();
                phone = edtPhone.getText().toString();
                password = edtPassword.getEditText().getText().toString();

                if (validate(email, username, phone, password)) {
                    Query checkUsername = databaseReference.orderByChild("username").equalTo(username);

                    checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                edtUsername.setError("Username already exists");
                                edtUsername.requestFocus();
                            } else {
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    String avatarDefault = "https://firebasestorage.googleapis.com/v0/b/chat-application-2f4f4.appspot.com/o/avatar%2Favatar_default.jpg?alt=media&token=868844f5-1306-4d95-8b43-3bc4abd6e3e0";
                                                    User user = new User(username, username, email, password, phone, avatarDefault, email, "Online");
                                                    user.setID(firebaseAuth.getCurrentUser().getUid());

                                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                                startActivity(intentToLogin);
                                                            } else {
                                                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    edtEmail.setError("This email has been registered");
                                                    edtEmail.requestFocus();
                                                }
                                            }
                                        });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    public void init() {
        txtLoginNow = findViewById(R.id.txtLoginNow);
        edtUsername = findViewById(R.id.edtUsernameRegister);
        edtEmail = findViewById(R.id.edtEmailRegister);
        edtPhone = findViewById(R.id.edtMobileNumberRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        edtPassword.setErrorTextColor(ColorStateList.valueOf(Color.WHITE));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public boolean validate(String email, String username, String phone, String password) {
        if (email.isEmpty()) {
            edtEmail.setError("Cannot be empty");
            edtEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email is incorrect");
            edtEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Cannot be empty");
            edtPassword.requestFocus();
            return false;
        }
        if (password.length() < 8 || password.length() > 16) {
            edtPassword.setError("Password must be between 8-16 characters");
            edtPassword.requestFocus();
            return false;
        }
        if (!checkNum(password)) {
            edtPassword.setError("Password must have at least 1 number");
            edtPassword.requestFocus();
            return false;
        }
        if (!checkUpperCase(password)) {
            edtPassword.setError("Password must have at least 1 uppercase letter");
            edtPassword.requestFocus();
            return false;
        }
        if (checkWhiteSpace(password)) {
            edtPassword.setError("Password must not contain spaces");
            edtPassword.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkNum(String s) {
        String pattern = ".*\\d.*";
        return s.matches(pattern);
    }

    public static boolean checkUpperCase(String s) {
        String pattern = ".*\\w.*";
        return s.matches(pattern);
    }

    public static boolean checkWhiteSpace(String s) {
        return s.contains(" ");
    }
}