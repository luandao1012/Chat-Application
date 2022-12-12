package com.example.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeInboxActivity extends AppCompatActivity {
    TextView txtInbox, txtCommunity, lineInbox, lineCommunity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_inbox);

        txtInbox = findViewById(R.id.txtInboxHome);
        txtCommunity = findViewById(R.id.txtCommunityHome);
        lineInbox = findViewById(R.id.lineInboxHome);
        lineCommunity = findViewById(R.id.lineCommunityHome);
        txtInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineInbox.setVisibility(View.VISIBLE);
                lineCommunity.setVisibility(View.GONE);
            }
        });
        txtCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineInbox.setVisibility(View.GONE);
                lineCommunity.setVisibility(View.VISIBLE);
            }
        });
    }
}