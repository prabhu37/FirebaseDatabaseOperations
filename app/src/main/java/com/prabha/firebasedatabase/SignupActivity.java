package com.prabha.firebasedatabase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prabha.firebasedatabase.Pojo.Event;
import com.prabha.firebasedatabase.Pojo.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_age)
    EditText _ageText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_area)
    EditText _areaText;
    @BindView(R.id.input_shoes_used)
    EditText _inputShoes;
    @BindView(R.id.input_city)
    EditText _areaCity;
    @BindView(R.id.input_distance_ran)
    EditText _areaRanText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    private String email = "", name = "", mobile = "",gender = "",userType="1", shoes = "",password, reEnterPassword, age = "", city = "",createdAt="",area = "", longestDistanceRan = "";
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private ProgressDialog progressDialog;
    private List<Event> joinEvents = new ArrayList<>();
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void signup() {
        Log.d(TAG, "Signup");

        email = _emailText.getText().toString();
        name = _nameText.getText().toString();
        mobile = _mobileText.getText().toString();
        shoes = _inputShoes.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();
        age = _ageText.getText().toString();
        area = _areaText.getText().toString();
        city = _areaCity.getText().toString();
        longestDistanceRan = _areaRanText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();
        if (!validate()) {
            onSignupFailed();
            return;
        }
        // _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        checkForEmail(mobile, email);


    }

    private void OnAuth(FirebaseUser user) {
        getDate(new Date());
        Log.e("CreatedAt :", createdAt);
        if(rbMale.isChecked()){
            gender = "Male";
        }else {
            gender = "Female";
        }
        createAnewUser(user.getUid());
    }

     private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
    }


    private User BuildNewuser() {
        return new User(
                email,
                name,
                mobile,
                age,
                city,
                area,
                shoes,
                gender,
                longestDistanceRan,
                createdAt,
                userType,
                joinEvents


        );
    }

    public void onSignupSuccess() {
        Intent intent = new Intent(SignupActivity.this, LocationActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("area", area);
        intent.putExtra("mobile",mobile);
        startActivity(intent);
        finish();
    }

    public String getDate(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        createdAt = sdf.format(date);
        return sdf.format(date);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (age.isEmpty()) {
            _ageText.setError("Enter the age");
            valid = false;
        } else {
            if (Integer.parseInt(age) < 16) {
                valid = false;
                _ageText.setError("Age have minimum 16");
            } else {
                _ageText.setError(null);
            }
        }

        if (area.isEmpty()) {
            _areaText.setError("Enter the area");
            valid = false;
        } else {
            _areaText.setError(null);
        }


        if (city.isEmpty()) {
            _areaCity.setError("Enter the city");
            valid = false;
        } else {
            _areaCity.setError(null);
        }

        if (longestDistanceRan.isEmpty()) {
            _areaRanText.setError("Enter the Longest distance ran");
            valid = false;
        } else {
            _areaRanText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    public void checkForPhoneNumber(String number) {
        Log.e("MobileNumber :", number);
        Query query = mdatabase.orderByChild("mobile").equalTo(number);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                OnAuth(task.getResult().getUser());
                                mAuth.signOut();
                                onSignupSuccess();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this, "error on creating user", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, mobile + " already registered", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }


        });

    }


    public void checkForEmail(String number, String email) {
        Log.e("MobileNumber :", email);
        Query query = mdatabase.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    checkForPhoneNumber(number);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, email + " already registered", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }


        });

    }
}