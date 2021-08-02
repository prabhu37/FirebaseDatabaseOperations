package com.prabha.firebasedatabase;

import static com.prabha.firebasedatabase.LoginActivity.mobileNo;
import static com.prabha.firebasedatabase.LoginActivity.userType;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.prabha.firebasedatabase.Pojo.Event;
import com.prabha.firebasedatabase.Pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class EventDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvEventName)
    TextView tvEvent;
    @BindView(R.id.tvArea)
    TextView tvLocation;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvVenue)
    TextView tvVenue;
    @BindView(R.id.tvCreatedBy)
    TextView tvCreatedBy;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvMembers)
    TextView tvMembers;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.lt_members)
    LinearLayout ltMembers;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.btn_join)
    AppCompatButton btJoin;
    String eventId;
    Event event = new Event();
    private List<Event> joinEvents = new ArrayList<>();
    private List<User> joinUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Events").child(Objects.requireNonNull(getIntent().getStringExtra("event_id")));
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);
                assert event != null;
                setDetails(event);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(EventDetailsActivity.this, LocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else if(userType.equalsIgnoreCase("0")){
                    Intent intent = new Intent(EventDetailsActivity.this, AdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("joinEvents");
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        joinEvents.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);
                            joinEvents.add(event);
                            Log.e("JoinEvents :", new Gson().toJson(joinEvents));

                        }
                        joinEvents.add(event);
                        // Set updated friends list
                        usersRef.setValue(joinEvents);

                        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId).child("joinUsers");
                        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                joinUsers.clear();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    User user = postSnapshot.getValue(User.class);
                                    joinUsers.add(user);
                                    Log.e("JoinEvents :", new Gson().toJson(joinEvents));

                                }
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
                                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        joinUsers.add(user);
                                        // Set updated friends list
                                        eventRef.setValue(joinUsers);
                                        Toast.makeText(EventDetailsActivity.this, "Thanks for joined this event!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Failed to read value
                                Log.e("MainPage :", "Failed to read value.", error.toException());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.e("MainPage :", "Failed to read value.", error.toException());
                    }
                });
            }
        });

    }

    public void setDetails(Event event) {
        this.event = event;
        tvEvent.setText(event.getEventName());
        tvDate.setText(event.getStartDate());
        tvLocation.setText(event.getArea());
        eventId = event.getEventId();
        tvCreatedBy.setText(event.getCreatedAt());
        tvDesc.setText(event.getDescription());
        tvDistance.setText(event.getDistance());
        tvVenue.setText(event.getVenue());

        Log.e("JoinUsers :", new Gson().toJson(event.getJoinUsers()));
        if(userType.equalsIgnoreCase("0")){
            btJoin.setVisibility(View.GONE);
        }else {
            btJoin.setVisibility(View.VISIBLE);
        }

        if (event.getJoinUsers() != null) {
            for (User user : event.getJoinUsers()) {
                if (user.getMobile().equalsIgnoreCase(mobileNo)) {
                    if(userType.equalsIgnoreCase("1"))
                    btJoin.setVisibility(View.GONE);
                    break;
                }
            }
            if (event.getJoinUsers().size() > 0) {
                ltMembers.setVisibility(View.VISIBLE);
            } else {
                ltMembers.setVisibility(View.GONE);
            }
        } else {
            ltMembers.setVisibility(View.GONE);
        }

        tvMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailsActivity.this, UsersDetailsActivity.class);
                intent.putExtra("event_id", event.getEventId());
                startActivity(intent);
            }
        });
    }
}
