package com.prabha.firebasedatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prabha.firebasedatabase.Adapters.AreaAdapter;
import com.prabha.firebasedatabase.Pojo.Event;
import com.prabha.firebasedatabase.Pojo.LocationSelectPojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class LocationActivity extends AppCompatActivity {
    private AreaAdapter areaAdapter;
    @BindView(R.id.rv_locations)
    RecyclerView rvLocations;
    private List<LocationSelectPojo> areaList = new ArrayList<>();
    public String selectedArea = "";
    private ProgressDialog progressDialog;
    private DatabaseReference mdatabase;
    @BindView(R.id.btn_confirm)
    AppCompatButton btConfirm;
    @BindView(R.id.tvLogOut)
    TextView tvLogOut;
    @BindView(R.id.tvProfile)
    TextView tvProfile;
    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);


        areaAdapter = new AreaAdapter(this, areaList);
        rvLocations.setLayoutManager(new GridLayoutManager(this, 2));
        rvLocations.setAdapter(areaAdapter);
        areaAdapter.notifyDataSetChanged();
        areaAdapter.setAreaSelectListener(new AreaAdapter.AreaSelectedListener() {
            @Override
            public void selectItem(int position) {
                if (areaList.get(position).isSelected()) {
                    areaList.get(position).setSelected(false);

                } else {
                    selectedArea = areaList.get(position).getLocation();
                    areaList.get(position).setSelected(true);
                    for (int i = 0; i < areaList.size(); i++) {
                        if (i != position) {
                            areaList.get(i).setSelected(false);
                        }
                    }
                }
                areaAdapter.notifyDataSetChanged();



            }
        });
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        getLocations();

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedArea.equalsIgnoreCase("")) {
                    Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                    intent.putExtra("area_1", selectedArea);
                    startActivity(intent);
                }else {
                    Toast.makeText(LocationActivity.this,"Please select a location",Toast.LENGTH_LONG).show();
                }

            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationActivity.this,UserEventsActivity.class);
                startActivity(intent);
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LocationActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    public void getLocations() {
        progressDialog = new ProgressDialog(LocationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Events...");
        progressDialog.show();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                areaList.clear();
                Set<String> areasSet = new HashSet<String>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    assert event != null;
                    areasSet.add(event.getArea());

                }
                List<String> tempArrayList = new ArrayList<>();
                tempArrayList.addAll(areasSet);
                for (int i = 0; i < tempArrayList.size(); i++) {
                    LocationSelectPojo locationSelectPojo = new LocationSelectPojo();
                    locationSelectPojo.setSelected(false);
                    locationSelectPojo.setLocation(tempArrayList.get(i));
                    areaList.add(locationSelectPojo);
                }

                areaAdapter.notifyDataSetChanged();
                if(areaList.size()>0){
                    tvInfo.setVisibility(View.GONE);
                }else {
                    tvInfo.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }
}
