package com.prabha.firebasedatabase;

import static com.prabha.firebasedatabase.LoginActivity.userType;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prabha.firebasedatabase.Pojo.User;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class UserEventsActivity extends AppCompatActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_shoes)
    TextView tvShoes;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);
        ButterKnife.bind(this);
        if(getIntent()!=null&& getIntent().getStringExtra("name")!=null){
            tvName.setText("Hai "+ getIntent().getStringExtra("name"));
        }

        DatabaseReference usersRef =   FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                // Set updated friends list
                assert user != null;
                setProfileDetails(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userType.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(UserEventsActivity.this, LocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else if(userType.equalsIgnoreCase("0")){
                    Intent intent = new Intent(UserEventsActivity.this, AdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void  setProfileDetails(User user){

        tvName.setText("Name : "+user.getName());
        tvEmail.setText("Email : "+user.getEmail());
        tvMobile.setText("Mobile : "+user.getMobile());
        tvAge.setText("Age : "+user.getAge());
        tvDistance.setText("Longest Distance Run(Km) : "+user.getLongestDistanceRan());
        tvShoes.setText("Shoes : "+user.getShoes());
        tvLocation.setText("Location :"+user.getArea());

    }
}
