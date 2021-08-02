package com.prabha.firebasedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.prabha.firebasedatabase.Adapters.UserAdapter;
import com.prabha.firebasedatabase.Pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class UsersDetailsActivity extends AppCompatActivity {

    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;
    List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter;
    private DatabaseReference mdatabase;
    @BindView(R.id.tvHome)
    TextView tvHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_details);
        ButterKnife.bind(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(Objects.requireNonNull(getIntent().getStringExtra("event_id"))).child("joinUsers");
        userAdapter = new UserAdapter(this,userList);
        rvUsers.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvUsers.setAdapter(userAdapter);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    assert user != null;
                    if(user.getUserType().equalsIgnoreCase("1")) {
                        userList.add(user);
                    }

                }
                userAdapter.notifyDataSetChanged();
                Log.e("event :", new Gson().toJson(userAdapter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersDetailsActivity.this, LocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
