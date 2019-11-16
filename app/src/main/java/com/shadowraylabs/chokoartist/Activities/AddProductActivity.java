package com.shadowraylabs.chokoartist.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shadowraylabs.chokoartist.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    ImageView productImage;
    EditText productName, productDescription, productPrice;
    Button addProductBtn;
    StorageReference productImagesRef;
    String downloadImageUrl;
    ProgressDialog progressDialog;

    static final int galleryPick = 1;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productImage = (ImageView) findViewById(R.id.add_product_image);
        productName = (EditText) findViewById(R.id.add_product_name);
        productDescription = (EditText) findViewById(R.id.add_product_description);
        productPrice = (EditText) findViewById(R.id.add_product_price);
        addProductBtn = (Button) findViewById(R.id.add_product_btn);
        productImagesRef = FirebaseStorage.getInstance().getReference().child("ProductImages");
        progressDialog = new ProgressDialog(AddProductActivity.this);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, galleryPick);
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, desc, price;
                name = productName.getText().toString();
                desc = productDescription.getText().toString();
                price = productPrice.getText().toString();
                validateProductData(name, desc, price);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == galleryPick && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            productImage.setImageURI(imageUri);
        }
    }

    public void validateProductData(final String name, final String desc, final String price){
        if(imageUri == null)
            Toast.makeText(AddProductActivity.this, "Please choose a product image", Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(name))
            Toast.makeText(AddProductActivity.this, "Product Name is required", Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(desc))
            Toast.makeText(AddProductActivity.this, "Product Description is required", Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(price))
            Toast.makeText(AddProductActivity.this, "Product Name is required", Toast.LENGTH_LONG).show();
        else{

            progressDialog.setTitle("Chokoartist");
            progressDialog.setMessage("Please wait while we add the product...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            final String saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            final String saveCurrentTime = currentTime.format(calendar.getTime());

            final String productKey = saveCurrentDate + saveCurrentTime;

            final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productKey + ".jpg");

            final UploadTask uploadTask = filePath.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(AddProductActivity.this, "Errror: "+message, Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddProductActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            downloadImageUrl = filePath.getDownloadUrl().toString();
                            return  filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                downloadImageUrl = task.getResult().toString();
                                Toast.makeText(AddProductActivity.this, "Got Product Image Url Successfully...", Toast.LENGTH_SHORT).show();
                                HashMap<String, Object> productMap = new HashMap<>();
                                productMap.put("pid", productKey);
                                productMap.put("date", saveCurrentDate);
                                productMap.put("time", saveCurrentTime);
                                productMap.put("description", desc);
                                productMap.put("price", price);
                                productMap.put("image", downloadImageUrl);
                                productMap.put("pname", name);
                                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
                                productsRef.child(productKey).updateChildren(productMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if(task.isSuccessful()){
                                                    Toast.makeText(AddProductActivity.this, "Product Added Successfully", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    Toast.makeText(AddProductActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                });
                            }
                        }
                    });
                }
            });
        }
    }
}
