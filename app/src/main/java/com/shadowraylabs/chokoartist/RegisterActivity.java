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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName, inputPhoneNo, inputPassword;
    private Button registerAccount;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = (EditText) findViewById(R.id.register_name);
        inputPhoneNo = (EditText) findViewById(R.id.register_phone_no);
        inputPassword = (EditText) findViewById(R.id.register_password);
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
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("phoneNo", phoneNo);
                    map.put("name", name);
                    map.put("password", password);
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
