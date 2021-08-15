package com.example.meetingreminder;

import java.util.Date;

public class Meeting {
    String id;
    String meetingName;
    String meetingDesc;
    String meetingPlace;
    String meetingCategories;
    Date meetingDate;

    public Meeting(){
    }

    public Meeting(String id, String meetingName, String meetingDesc, String meetingPlace, String meetingCategories, Date meetingDate) {
        this.id = id;
        this.meetingName = meetingName;
        this.meetingDesc = meetingDesc;
        this.meetingPlace = meetingPlace;
        this.meetingCategories = meetingCategories;
        this.meetingDate = meetingDate;
    }
}
