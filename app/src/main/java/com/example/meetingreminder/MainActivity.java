package com.example.meetingreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        sharedPref = this.getSharedPreferences("shared_name", Context.MODE_PRIVATE);

        String userName = sharedPref.getString("name","");

        if(userName.length() == 0) {
            startActivity(new Intent(this, GetStartedActivity.class));
            finish();
        }else{
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}