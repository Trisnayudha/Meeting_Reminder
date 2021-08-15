package com.example.meetingreminder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<Meeting> meetings;
    Context context;
    DatabaseHelper db;
    public RecyclerViewAdapter(List<Meeting> meetings, Context context) {
        this.meetings = meetings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meeting_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        db = new DatabaseHelper(context);
        //
        String dateTime = new SimpleDateFormat("EEEE, MMM d yyyy").format(meetings.get(position).meetingDate);
        String dateTimeAbove = "";

        //get Time from date
        String hourStart = gethour(meetings.get(position).meetingDate);

        //
        if (position != 0) {
            dateTimeAbove = new SimpleDateFormat("EEEE, MMM d yyyy").format(meetings.get(position - 1).meetingDate);
            if (dateTime.equals(dateTimeAbove)) {
                holder.textViewDate.setVisibility(View.GONE);
            }
        }
        holder.textViewDate.setText(dateTime);
        holder.textViewTitle.setText(meetings.get(position).meetingName);
        holder.textViewTimeStart.setText(hourStart);
        holder.textViewPlace.setText(meetings.get(position).meetingPlace);
        holder.textViewDescription.setText(meetings.get(position).meetingDesc);
        holder.textViewCategories.setText(meetings.get(position).meetingCategories);

        holder.cardMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateMeetingActivity.class);

                intent.putExtra("editableMeeting", meetings.get(position).id);

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        LinearLayout cardMeeting;
        TextView textViewTitle;
        TextView textViewTimeStart;
        TextView textViewDescription;
        TextView textViewCategories;
        TextView textViewPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.tv_date);
            cardMeeting = itemView.findViewById(R.id.card_meeting);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewTimeStart = itemView.findViewById(R.id.tv_timeStart);
            textViewDescription = itemView.findViewById(R.id.tv_desc);
            textViewCategories = itemView.findViewById(R.id.tv_categories);
            textViewPlace = itemView.findViewById(R.id.tv_place);
        }
    }

    private String gethour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String hour = (String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1) ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = (String.valueOf(calendar.get(Calendar.MINUTE)).length() == 1) ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
        String hourAndMinute = hour + ":" + minute;
        return hourAndMinute;
    }
}
