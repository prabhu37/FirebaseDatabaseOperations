package com.prabha.firebasedatabase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.prabha.firebasedatabase.Adapters.AreaAdapter;
import com.prabha.firebasedatabase.Adapters.EventsAdapter;
import com.prabha.firebasedatabase.Pojo.Event;
import com.prabha.firebasedatabase.Pojo.LocationSelectPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */

public class MainActivity extends AppCompatActivity {


    private List<LocationSelectPojo> areaList = new ArrayList<>();
    private Dialog areaListDialog;
    @BindView(R.id.lt_select_area)
    LinearLayout ltSelectedArea;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_no_events)
    TextView tvNoEvents;
    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;
    private AreaAdapter areaAdapter;
    private EventsAdapter eventsAdapter;
    List<Event> events = new ArrayList<>();

    private DatabaseReference mdatabase;
    private String userAra="";
    private String userId;
    private ProgressDialog progressDialog;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        areaAdapter = new AreaAdapter(this,areaList);
        eventsAdapter = new EventsAdapter(this,events,true);
        rvEvents.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvEvents.setAdapter(eventsAdapter);
        eventsAdapter.setEventsListener(new EventsAdapter.EventsListener() {
            @Override
            public void jonEvent(int position) {
                Intent intent = new Intent(MainActivity.this,EventDetailsActivity.class);
                intent.putExtra("event_id",events.get(position).getEventId());
                startActivity(intent);
             /*   Log.e("EventJoin :","Clicked!");
                DatabaseReference usersRef =   FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("joinEvents");
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        joinEvents.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);
                            joinEvents.add(event);
                            Log.e("JoinEvents :", new Gson().toJson(joinEvents));

                        }
                        joinEvents.add(events.get(position));
                        // Set updated friends list
                        usersRef.setValue(joinEvents);

                       DatabaseReference eventRef =   FirebaseDatabase.getInstance().getReference().child("Events").child(events.get(position).getEventId()).child("joinUsers");
                        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                joinUsers.clear();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    User user = postSnapshot.getValue(User.class);
                                    joinUsers.add(user);
                                    Log.e("JoinEvents :", new Gson().toJson(joinEvents));

                                }
                                DatabaseReference usersRef =   FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
                                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        joinUsers.add(user);
                                        // Set updated friends list
                                        eventRef.setValue(joinUsers);
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
                });*/



            }
        });
        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        if(getIntent()!=null){
            if(getIntent().getStringExtra("area_1")!=null){
                userAra = getIntent().getStringExtra("area_1");
                tvName.setText("Events in "+getIntent().getStringExtra("area_1"));
            }
            if(getIntent().getStringExtra("mobile_1")!=null){
                userId = getIntent().getStringExtra("mobile_1");
            }

        }

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        getEvents(true);
        ltSelectedArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAreaLisListDialog();
            }
        });

    }

    public void  getEvents(boolean defaultArea){
        progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Events...");
        progressDialog.show();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 progressDialog.dismiss();
                 events.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    assert event != null;
                    if(event.getArea().equalsIgnoreCase(userAra)){
                        events.add(event);
                    }

                }
                eventsAdapter.notifyDataSetChanged();
                Log.e("event :", new Gson().toJson(areaList));
                if(events.size()==0){
                    tvNoEvents.setVisibility(View.VISIBLE);
                }else {
                    tvNoEvents.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }
    private void showAreaLisListDialog() {
        areaListDialog = new Dialog(this, R.style.AppTheme_Dark_Dialog);
        areaListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        areaListDialog.setCancelable(true);
        areaListDialog.setContentView(R.layout.dialog_area_list);
        RecyclerView rvPrice = areaListDialog.findViewById(R.id.rv_events);
        rvPrice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPrice.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
       /* areaAdapter.setAreaSelectListener(new AreaAdapter.AreaSelectedListener() {
            @Override
            public void selectItem(int position) {
                areaListDialog.dismiss();
                userAra = areaList.get(position);
                tvName.setText(userAra);
                getEvents(false);
            }
        });*/
        areaListDialog.show();
        Window windo = areaListDialog.getWindow();
        Objects.requireNonNull(windo).setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }


}
