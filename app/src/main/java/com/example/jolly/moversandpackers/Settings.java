package com.example.jolly.moversandpackers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity
        implements android.support.design.widget.NavigationView.OnNavigationItemSelectedListener {
    ListView lv;
    String set[]={"Profile","Change Password"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv=(ListView)findViewById(R.id.list);

        MyAdapter adapter1=new MyAdapter(getApplicationContext(),set);
        lv.setAdapter(adapter1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        android.support.design.widget.NavigationView navigationView = (android.support.design.widget.NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent intent=new Intent(Settings.this,Customer_profile.class);
                        startActivity(intent);
                        break;
                    case 1 :
                        Intent i=new Intent(Settings.this,Change_password.class);
                        startActivity(i);
                        break;
                }
            }
        });
    }
class MyAdapter extends ArrayAdapter{
    String[] name;

    public MyAdapter(Context context, String[] name) {
        super(context, R.layout.listset, R.id.profileid,name);
        this.name = name;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.listset,parent,false);
        TextView name1=(TextView)row.findViewById(R.id.profileid);
        name1.setText(name[position]);
        return super.getView(position, convertView, parent);
    }
}
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Intent i = new Intent(Settings.this, Customer_main.class);
            startActivity(i);
        } else if (id == R.id.nav_contact_us) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if (ActivityCompat.checkSelfPermission(Settings.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Settings.this, "grant the permission", Toast.LENGTH_SHORT).show();
                requestPermission();
            } else {
                startActivity(callIntent);
            }
        }
            else if (id == R.id.nav_feedback) {
            Intent intent=new Intent(Settings.this,Feedback.class);
            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            Intent intent=new Intent(Settings.this,Visitor_main.class);
            startActivity(intent);
        }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
}
    private void requestPermission() {
        ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

}

