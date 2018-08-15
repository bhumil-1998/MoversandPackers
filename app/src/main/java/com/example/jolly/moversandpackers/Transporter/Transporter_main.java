package com.example.jolly.moversandpackers.Transporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jolly.moversandpackers.Contact_us;
import com.example.jolly.moversandpackers.Customer_profile;
import com.example.jolly.moversandpackers.Feedback;
import com.example.jolly.moversandpackers.GlobalVariable;
import com.example.jolly.moversandpackers.Order_customer;
import com.example.jolly.moversandpackers.Quotation_customer;
import com.example.jolly.moversandpackers.R;
import com.example.jolly.moversandpackers.Request_customer;
import com.example.jolly.moversandpackers.Shifting;
import com.example.jolly.moversandpackers.Visitor_main;

public class Transporter_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transporter_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_sign_out_transporter) {
            GlobalVariable.uname="";
            Intent intent=new Intent(this,Visitor_main.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile_transporter) {
            Intent intent=new Intent(this,Customer_profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent =new Intent(this,Contact_us.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onButtonClicker(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.tran_main_btn_request:
                intent = new Intent(this,Request_transporter.class);
                startActivity(intent);
                break;
            case R.id.trans_main_btn_quotation:
                intent = new Intent(this, Quotation_transporter.class);
                startActivity(intent);
                break;
            case R.id.trans_main_btn_order:
                intent = new Intent(this, Order_transporter.class);
                startActivity(intent);
                break;
            case R.id.trans_main_feedback_btn:
                intent=new Intent(this,Feedback.class);
                startActivity(intent);

        }
    }
}
