package com.prabha.firebasedatabase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prabha.firebasedatabase.Pojo.Event;
import com.prabha.firebasedatabase.Pojo.User;
import com.prabha.firebasedatabase.Utils.CustomDateTimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class AddEventsActivity extends AppCompatActivity {
    private String eventName = "";
    private String startDate = "";
    private String area = "";
    private String eventId = "";
    private String venue = "";
    private String createdAt = "";
    private String distance = "";
    private String description = "";

    @BindView(R.id.input_event)
    EditText _textEvent;
    @BindView(R.id.input_area)
    EditText _textArea;
    @BindView(R.id.input_date)
    EditText _textDate;
    @BindView(R.id.input_venue)
    EditText _textVenue;
    @BindView(R.id.input_distance)
    EditText _textDistance;
    @BindView(R.id.input_description)
    EditText _textDescription;

    CustomDateTimePicker custom;
    private DatabaseReference mdatabase;
    @BindView(R.id.btn_post)
    AppCompatButton btAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);
        ButterKnife.bind(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        setCustomeCalendar();
        _textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom.showDialog();
            }
        });
        btAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });

    }

    public void addEvent() {
        eventName = _textEvent.getText().toString().trim();
        area = _textArea.getText().toString().trim();
        startDate = _textDate.getText().toString().trim();
        venue = _textVenue.getText().toString().trim();
        distance = _textDistance.getText().toString().trim();
        description = _textDescription.getText().toString().trim();
        if (TextUtils.isEmpty(eventName)) {
            Toast.makeText(AddEventsActivity.this, "Enter the event name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(area)) {
            Toast.makeText(AddEventsActivity.this, "Enter the area", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(distance)) {
            Toast.makeText(AddEventsActivity.this, "Enter the distance", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(description)) {
            Toast.makeText(AddEventsActivity.this, "Enter the description", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(venue)) {
            Toast.makeText(AddEventsActivity.this, "Enter the venue", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(startDate)) {
            Toast.makeText(AddEventsActivity.this, "Enter the start date", Toast.LENGTH_SHORT).show();
            return;
        } else {
            createAnewEvent();
            Toast.makeText(AddEventsActivity.this, "New event posted!", Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 1000);
        }


    }

    public void setCustomeCalendar() {
        custom = new CustomDateTimePicker(this,
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int day,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        //                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        _textDate.setText("");
                        String minText, hoursText, dayText;
                        if (min < 10) {
                            minText = "0" + min;
                        } else {
                            minText = min + "";
                        }
                        if (hour12 < 10) {
                            hoursText = "0" + hour12;
                        } else {
                            hoursText = hour12 + "";
                        }
                        if (calendarSelected.get(Calendar.DAY_OF_MONTH) < 10) {
                            dayText = "0" + calendarSelected.get(Calendar.DAY_OF_MONTH);
                        } else {
                            dayText = calendarSelected.get(Calendar.DAY_OF_MONTH) + "";
                        }
                        _textDate.setText(monthShortName.toLowerCase() + " " + dayText+","+year
                                + " " + hoursText + ":" + minText + " " + AM_PM);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

        custom.set24HourFormat(false);
        custom.setDate(Calendar.getInstance());

    }

    private void createAnewEvent() {
        eventId = new Date().getTime() + "";
        getDate(new Date());
        Event event = BuildNewEvent();
        mdatabase.child(eventId).setValue(event);
    }

    public void getDate(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        createdAt = sdf.format(date);
        sdf.format(date);
    }

    private Event BuildNewEvent() {
        List<User> joinUsers = new ArrayList<User>();
        return new Event(
                eventName,
                area,
                startDate,
                venue,
                createdAt,
                distance,
                description,
                joinUsers,
                eventId

        );
    }

}
