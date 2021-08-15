package com.example.meetingreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //
    ImageButton btn_delete;
    TextView tv_createOrDelete;
    ImageView btn_back;
    ImageButton btn_datePick;
    TextView tv_datePick;
    ImageButton btn_timePick;
    TextView tv_TimePick;
    Button btn_createMeeting;
    EditText et_title;
    EditText et_place;
    EditText et_desc;
    EditText et_categories;
    //
    Calendar calendar = Calendar.getInstance();
    String title;
    String place;
    String desc;
    String categorie;
    Date selectedDate;
    DatabaseHelper db;

    String editableMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        //intitValue
        btn_delete = findViewById(R.id.btn_delete);
        btn_back = findViewById(R.id.btn_back);
        btn_datePick = findViewById(R.id.btn_calendar);
        tv_datePick = findViewById(R.id.tv_calendar);
        btn_timePick = findViewById(R.id.btn_clock);
        tv_TimePick = findViewById(R.id.tv_clock);
        btn_createMeeting = findViewById(R.id.btn_create_meeting);
        et_title = findViewById(R.id.et_title);
        et_place = findViewById(R.id.et_place);
        et_desc = findViewById(R.id.et_desc);
        et_categories = findViewById(R.id.et_categories);
        tv_createOrDelete = findViewById(R.id.tv_create_or_delete);
        //
        db = new DatabaseHelper(getApplicationContext());

        btn_delete.setVisibility(View.GONE);

        editableMeeting = getIntent().getStringExtra("editableMeeting");
        if (editableMeeting != null) {
            Meeting meeting = db.getSingleMeeting(editableMeeting);
            tv_createOrDelete.setText("Edit \nMeeting");
            btn_delete.setVisibility(View.VISIBLE);
            btn_createMeeting.setText("Update Meeting");
            et_title.setText(meeting.meetingName);
            et_desc.setText(meeting.meetingDesc);
            et_categories.setText(meeting.meetingCategories);
            et_place.setText(meeting.meetingPlace);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(meeting.meetingDate);

            String hour = (String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1) ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            String minuteOfHour = (String.valueOf(calendar.get(Calendar.MINUTE)).length() == 1) ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
            String hourAndMinute = hour + ":" + minuteOfHour;
            String dateFormated = new SimpleDateFormat("EEEE, MMM d yyyy").format(calendar.getTime());

            tv_datePick.setText((dateFormated == null) ? "Null pak" : dateFormated);
            tv_TimePick.setText(hourAndMinute);
        }
        //set TextView Date and Time to this DateTime.now
        String hour = (String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1) ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minuteOfHour = (String.valueOf(calendar.get(Calendar.MINUTE)).length() == 1) ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
        String hourAndMinute = hour + ":" + minuteOfHour;
        String dateFormated = new SimpleDateFormat("EEEE, MMM d yyyy").format(calendar.getTime());

        tv_datePick.setText((dateFormated == null) ? "Null pak" : dateFormated);
        tv_TimePick.setText(hourAndMinute);
        //btn delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteMeeting(editableMeeting);
                finish();
            }
        });
        //appbar backbutton
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //btn calendar
        btn_datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        //btn Clock
        btn_timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time Picker");
            }
        });
        //btn create meeting
        btn_createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedDate = calendar.getTime();
                title = String.valueOf(et_title.getText());
                place = String.valueOf(et_place.getText());
                desc = String.valueOf(et_desc.getText());
                categorie = String.valueOf(et_categories.getText());

                Date currentDate = Calendar.getInstance().getTime();

                if (selectedDate.compareTo(currentDate) > 0) {
                    if (editableMeeting == null) {

                        Meeting meeting = new Meeting(
                                String.valueOf(selectedDate.getTime()),
                                title,
                                desc, place, categorie, selectedDate
                        );
                        db.addMeeting(meeting);
                    } else {
                        Meeting meeting = new Meeting(
                                editableMeeting,
                                title,
                                desc, place, categorie, selectedDate
                        );
                        db.updateMeting(meeting);
                    }

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);


                    Calendar c = Calendar.getInstance();
                    c.setTime(selectedDate);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) c.getTimeInMillis(), intent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() - (3600 * 1000), pendingIntent);

                    finish();
                }else{
                    Toast.makeText(CreateMeetingActivity.this, "Can't make meeting in the Past", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dateFormated = new SimpleDateFormat("EEEE, MMM d yyyy").format(calendar.getTime());
        tv_datePick.setText((dateFormated == null) ? "Null pak" : dateFormated);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        String hour = (String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1) ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minuteOfHour = (String.valueOf(calendar.get(Calendar.MINUTE)).length() == 1) ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
        String hourAndMinute = hour + ":" + minuteOfHour;
        tv_TimePick.setText(hourAndMinute);


    }
}