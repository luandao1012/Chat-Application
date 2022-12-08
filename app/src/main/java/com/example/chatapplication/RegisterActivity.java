package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapplication.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView txtLoginNow = findViewById(R.id.txtLoginNow);
        EditText edtUsername = findViewById(R.id.edtUsernameRegister);
        EditText edtEmail = findViewById(R.id.edtEmailRegister);
        EditText edtPhone = findViewById(R.id.edtMobileNumberRegister);
        TextInputLayout edtPassword = findViewById(R.id.edtPasswordRegister);
        Button btnRegister = findViewById(R.id.btnRegister);


        Intent intentToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
        txtLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentToLogin);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("users");
                firebaseAuth = FirebaseAuth.getInstance();

                String username, email, phone, password;
                username = edtUsername.getText().toString();
                email = edtEmail.getText().toString();
                phone = edtPhone.getText().toString();
                password = edtPassword.getEditText().getText().toString();


                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user = new User();
                                    user.setUsername(username);
                                    user.setEmail(email);
                                    user.setPassword(password);
                                    user.setPhone(phone);

                                    databaseReference.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                startActivity(intentToLogin);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                                }
                            }
                       } );
            }
        });
    }
}