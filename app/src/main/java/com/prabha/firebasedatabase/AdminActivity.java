package com.prabha.firebasedatabase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.prabha.firebasedatabase.Adapters.EventsAdapter;
import com.prabha.firebasedatabase.Pojo.Event;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class AdminActivity extends AppCompatActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tvLogOut)
    TextView tvLogOut;
    @BindView(R.id.tvNewEvent)
    TextView btnAddEvents;
    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;
    private EventsAdapter eventsAdapter;
    private ProgressDialog progressDialog;
    List<Event> events = new ArrayList<>();
    private DatabaseReference mdatabase;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        tvName.setText("Admin");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        eventsAdapter = new EventsAdapter(this,events,false);
        rvEvents.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvEvents.setAdapter(eventsAdapter);
        eventsAdapter.setEventsListener(new EventsAdapter.EventsListener() {
            @Override
            public void jonEvent(int position) {
                Intent intent = new Intent(AdminActivity.this,EventDetailsActivity.class);
                intent.putExtra("event_id",events.get(position).getEventId());
                startActivity(intent);
            }
        });
        btnAddEvents.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(AdminActivity.this,AddEventsActivity.class);
               startActivity(intent);
           }
       });

        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(AdminActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Events Loading...");
        progressDialog.show();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                events.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    events.add(event);

                }
                eventsAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                Log.e("event :", new Gson().toJson(events));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }


}
