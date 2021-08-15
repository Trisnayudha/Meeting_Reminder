package com.example.meetingreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_meeting_reminder";
    private static final String TABLE_NAME = "tb_meetings";
    private static final String KEY_MEETING_ID = "tb_meetingsId";
    private static final String KEY_MEETING_NAME = "db_meetingsName";
    private static final String KEY_MEETING_DESC = "db_meetingsDesc";
    private static final String KEY_MEETING_PLACE = "db_meetingsPlace";
    private static final String KEY_MEETING_CATEGORI = "db_meetingsCategori";
    private static final String KEY_MEETING_DATE = "db_meetingsDate";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DATABASE = " CREATE TABLE " + TABLE_NAME + " ("
                + KEY_MEETING_ID + " TEXT PRIMARY KEY,"
                + KEY_MEETING_NAME + " TEXT,"
                + KEY_MEETING_DESC + " TEXT,"
                + KEY_MEETING_PLACE + " TEXT,"
                + KEY_MEETING_CATEGORI + " TEXT,"
                + KEY_MEETING_DATE + " REAL )";
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void addMeeting(Meeting meeting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEETING_ID, meeting.id);
        values.put(KEY_MEETING_NAME, meeting.meetingName);
        values.put(KEY_MEETING_DESC, meeting.meetingDesc);
        values.put(KEY_MEETING_PLACE, meeting.meetingPlace);
        values.put(KEY_MEETING_CATEGORI, meeting.meetingCategories);
        values.put(KEY_MEETING_DATE, meeting.meetingDate.getTime());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Meeting getSingleMeeting(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_MEETING_ID, KEY_MEETING_NAME, KEY_MEETING_DESC, KEY_MEETING_PLACE, KEY_MEETING_CATEGORI, KEY_MEETING_DATE}, KEY_MEETING_ID + "=?",
                new String[]{id}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Meeting meeting = new Meeting(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                new Date(cursor.getInt(5))
        );
        return meeting;
    };

    public List<Meeting> getMeetings() {
        List<Meeting> meetingList = new ArrayList<Meeting>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM "+ TABLE_NAME+" ORDER BY "+KEY_MEETING_DATE+" ASC";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
           do {
               Meeting meeting = new Meeting(
                       cursor.getString(0),
                       cursor.getString(1),
                       cursor.getString(2),
                       cursor.getString(3),
                       cursor.getString(4),
                       new Date(cursor.getLong(5))
               );
               meetingList.add(meeting);
           }while(cursor.moveToNext());
        }
        return meetingList;
    };

    public int updateMeting(Meeting meeting){
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_MEETING_ID, meeting.id);
        values.put(KEY_MEETING_NAME, meeting.meetingName);
        values.put(KEY_MEETING_DESC, meeting.meetingDesc);
        values.put(KEY_MEETING_PLACE, meeting.meetingPlace);
        values.put(KEY_MEETING_CATEGORI, meeting.meetingCategories);
        values.put(KEY_MEETING_DATE, meeting.meetingDate.getTime());

        return db.update(TABLE_NAME,values, KEY_MEETING_ID +"=?", new String[]{meeting.id});
    }

    public  void deleteMeeting(String meetingId){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_MEETING_ID +"=?", new String[]{meetingId});
        db.close();
    }
}
