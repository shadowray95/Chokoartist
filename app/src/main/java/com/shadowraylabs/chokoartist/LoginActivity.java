package com.shadowraylabs.chokoartist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shadowraylabs.chokoartist.Model.Users;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNo, password;
    private Button loginBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNo = (EditText) findViewById(R.id.phoneNo);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(phoneNo.getText().toString(), password.getText().toString());
            }
        });
    }

    public void loginUser(String phoneNo, String password){
        if(TextUtils.isEmpty(phoneNo))
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(password))
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
        else{
            progressDialog.setTitle("Chokoartist");
            progressDialog.setMessage("Please wait while we log you in...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            validateAndLogin(phoneNo, password);
        }
    }

    public void validateAndLogin(final String phoneNo, final String password){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phoneNo).exists()){
                    Users user = dataSnapshot.child("Users").child(phoneNo).getValue(Users.class);
                    if(user.getPassword().equals(password)){
                        Toast.makeText(LoginActivity.this, "Welcome "+user.getName(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Invalid Password. Please Try Again", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Phone Number is Not Registered. Please Register First!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
