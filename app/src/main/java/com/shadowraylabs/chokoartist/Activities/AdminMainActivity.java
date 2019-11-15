package com.shadowraylabs.chokoartist.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.shadowraylabs.chokoartist.Model.Users;
import com.shadowraylabs.chokoartist.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class AdminMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    TextView adminMainUserName, adminMainPhoneNo;

    Users userObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Write to us at sakshijaindelhi22@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cart, R.id.nav_orders,
                R.id.nav_account, R.id.nav_admin_panel, R.id.nav_share, R.id.contact_us)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        adminMainUserName = (TextView) headerView.findViewById(R.id.adminMainUserName);
        adminMainPhoneNo = (TextView) headerView.findViewById(R.id.adminMainPhoneNo);

        userObj = (Users) getIntent().getSerializableExtra("userObj");
        Toast.makeText(AdminMainActivity.this, "Welcome to Chokoartist "+userObj.getName(), Toast.LENGTH_LONG).show();
        adminMainUserName.setText(userObj.getName());
        adminMainPhoneNo.setText(userObj.getPhoneNo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(AdminMainActivity.this, "Logged Out Successfully.", Toast.LENGTH_LONG).show();
                Intent intent  = new Intent(AdminMainActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Users getUserObject(){
        return userObj;
    }
}
