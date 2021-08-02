package com.prabha.firebasedatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static String mobileNo = "";
    public static String userType = "";

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.rb_user)
    AppCompatRadioButton rbUser;
    @BindView(R.id.rb_admin)
    AppCompatRadioButton rbAdmin;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String email, password;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        if (mUser != null) {
            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    Log.e("Usertype :", Objects.requireNonNull(dataSnapshot.child("userType").getValue()).toString());
                    userType = Objects.requireNonNull(dataSnapshot.child("userType").getValue()).toString();
                    mobileNo = Objects.requireNonNull(dataSnapshot.child("mobile").getValue()).toString();
                    if(userType.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(LoginActivity.this, LocationActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("mobile",Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.child("mobile").getValue()).toString()));
                        intent.putExtra("area",Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.child("area").getValue()).toString()));
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();

                }

            });
        }
                    _loginButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if(rbAdmin.isChecked() || rbUser.isChecked()) {
                                login();
                            }else {
                                Toast.makeText(LoginActivity.this, "Please select user type above!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });
        }

        public void login () {
            Log.d(TAG, "Login");

            if (!validate()) {
                onLoginFailed();
                return;
            }

            //_loginButton.setEnabled(false);

            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            userSign();
            // TODO: Implement your own authentication logic here.
/*
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
        }
        private void userSign () {
            email = _emailText.getText().toString().trim();
            password = _passwordText.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Enter the correct Email", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Enter the correct password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login not successful", Toast.LENGTH_SHORT).show();

                    } else {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        ref.child(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                progressDialog.dismiss();
                                String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                                Log.e("Usertype :", Objects.requireNonNull(dataSnapshot.child("userType").getValue()).toString()+"");
                                 userType = Objects.requireNonNull(dataSnapshot.child("userType").getValue()).toString();
                                if(userType.equalsIgnoreCase("1")) {
                                    if(rbUser.isChecked()) {
                                        mobileNo = Objects.requireNonNull(dataSnapshot.child("mobile").getValue()).toString();
                                        Intent intent = new Intent(LoginActivity.this, LocationActivity.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("area",Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.child("area").getValue()).toString()));
                                        startActivity(intent);
                                        finish();

                                    }else {
                                        Toast.makeText(LoginActivity.this, "you clicked user type is admin. You are a participant!", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    }
                                }else {
                                    if(rbAdmin.isChecked()) {
                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        intent.putExtra("name", name);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "you clicked user type is participant. You are an admin!", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();

                            }

                        });


                    }
                }
            });

        }


        @Override
        public void onBackPressed () {
            // Disable going back to the MainActivity
            moveTaskToBack(true);
        }

        public void onLoginSuccess () {
            _loginButton.setEnabled(true);
            finish();
        }

        public void onLoginFailed () {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }

        public boolean validate () {
            boolean valid = true;

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("enter a valid email address");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                _passwordText.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;
        }
    }
