package com.example.meetingreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    TextView tv_username;
    SharedPreferences sharedPref;
    //
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    //
    List<Meeting> meetingList;
    FloatingActionButton btn_add;
    DatabaseHelper db;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //initView
        tv_username = findViewById(R.id.tv_username);
        sharedPref = this.getSharedPreferences("shared_name", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recycler_view);
        btn_add = findViewById(R.id.btn_add);

        //
        db = new DatabaseHelper(getApplicationContext());
        meetingList = new ArrayList<>();

        //get username from shared reference
        String userName = sharedPref.getString("name", "User");
        //set into textview
        tv_username.setText(userName);
        showMeetingList();
        //get all meetings
        meetingList = db.getMeetings();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateMeetingActivity.class));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        meetingList = db.getMeetings();
        showMeetingList();
    }

    void showMeetingList() {
        recyclerViewAdapter = new RecyclerViewAdapter(meetingList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}