package com.example.meetingreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetStartedActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    EditText edt_name;
    Button btn_get_started;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        //InitView
        edt_name = findViewById(R.id.et_name);
        btn_get_started = findViewById(R.id.btn_get_started);
        sharedPref = this.getSharedPreferences("shared_name",Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        btn_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_name.getText().toString().trim();
                if(username.length() != 0){
                    editor.putString("name", username);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter the Username, before Continue",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}