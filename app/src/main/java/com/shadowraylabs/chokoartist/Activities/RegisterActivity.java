package com.shadowraylabs.chokoartist.Activities;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shadowraylabs.chokoartist.Constants.AppConstants;
import com.shadowraylabs.chokoartist.Model.Users;
import com.shadowraylabs.chokoartist.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName, inputPhoneNo, inputPassword, referralPhone;
    private Button registerAccount;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = (EditText) findViewById(R.id.register_name);
        inputPhoneNo = (EditText) findViewById(R.id.register_phone_no);
        inputPassword = (EditText) findViewById(R.id.register_password);
        referralPhone = (EditText) findViewById(R.id.register_referral_phone);
        registerAccount = (Button) findViewById(R.id.register);
        progressDialog = new ProgressDialog(this);

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String phoneNo = inputPhoneNo.getText().toString();
                String password = inputPassword.getText().toString();
                registerAccount(name, phoneNo, password);
            }
        });
    }

    public void registerAccount(String name, String phoneNo, String password){
        if(TextUtils.isEmpty(name))
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(phoneNo))
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(password))
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
        else{
            progressDialog.setTitle("Chokoartist");
            progressDialog.setMessage("Please wait while we create your account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            validateAndCreateAccount(name, phoneNo, password);
        }
    }

    public void validateAndCreateAccount(final String name, final String phoneNo, final String password){
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phoneNo).exists()){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, phoneNo + " is already registered! Try Logging In.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    String referree = referralPhone.getText().toString();
                    if(dataSnapshot.child("Users").child(referree).exists()){
                        Users user = dataSnapshot.child("Users").child(referree).getValue(Users.class);
                        rootRef.child("Users").child(referree).child("noOfReferrals").setValue(user.getNoOfReferrals() + 1);

                        int noOfRefs = (user.getNoOfReferrals() + 1)%5;
                        if(noOfRefs == 0){
                            rootRef.child("Users").child(referree).child("storeCredits").setValue(user.getStoreCredits() + AppConstants.promotionPoints);
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, referralPhone + " is not registered.", Toast.LENGTH_LONG).show();
                    }


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("phoneNo", phoneNo);
                    map.put("name", name);
                    map.put("password", password);
                    map.put("noOfReferrals", 0);
                    map.put("storeCredits", 50);
                    rootRef.child("Users").child(phoneNo).updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Network Error Occured!", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
