package com.example.jolly.moversandpackers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class Visitor_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView feedback_visitor_listview;
    ArrayList<String> rating_value;
    ArrayList<String> name;
    ArrayList<String> feedback;
    ArrayList<String> feedback_id;
    ArrayList<String> date;
    ArrayAdapter<String> myadapter;

    String visitor_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Visitor");
        setContentView(R.layout.activity_visitor_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rating_value=new ArrayList<>();
        name=new ArrayList<>();
        feedback=new ArrayList<>();
        feedback_id=new ArrayList<>();
        date=new ArrayList<>();
        feedback_visitor_listview= (ListView) findViewById(R.id.feedback_display_visitor_listview);
        new getFeedbackVisitor().execute();
        feedback_visitor_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    default:
                        visitor_str = name.get(position);
                        Intent intent=new Intent(Visitor_main.this,Visitor_main_detail.class);
                        intent.putExtra("rating_value",rating_value.get(position));
                        intent.putExtra("name",name.get(position));
                        intent.putExtra("feedback",feedback.get(position));
                        intent.putExtra("date",date.get(position));
                        startActivity(intent);
                        break;
                }

            }

        });
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
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_login) {
            intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            intent=new Intent(this,Registration.class);
            startActivity(intent);
        }  else if (id == R.id.nav_contact_us_visitor) {
            intent=new Intent(this,Contact_us.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public class getFeedbackVisitor extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Visitor_main.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String link="http://packersandmovers-virali.000webhostapp.com/moversandpackers/getVisitorFeedback.php";
            String json= WebInterface.getData(link);
            Log.i("json",json);
            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            String json=s;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    int status=Integer.parseInt(jsonObject.getString("status"));
                    String message=jsonObject.getString("message");
                    if (status==1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            feedback_id.add(jsonObject1.getString("Feedback_id"));
                            date.add(jsonObject1.getString("Date"));
                            feedback.add(jsonObject1.getString("Description"));
                            rating_value.add(jsonObject1.getString("Rating_value"));
                            name.add(jsonObject1.getString("User_name"));
                        }

                    }
                    else {
                        Toast.makeText(Visitor_main.this, "", Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(Visitor_main.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }

            myadapter=new Myadapter(Visitor_main.this,name,rating_value,feedback,date);
            feedback_visitor_listview.setAdapter(myadapter);
            super.onPostExecute(s);
        }

    }
    class Myadapter extends ArrayAdapter{
        ArrayList<String > name=new ArrayList<>();
        ArrayList<String> rating_value=new ArrayList<>();
        ArrayList<String> feedbacktext=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();

        public Myadapter(Context context,ArrayList<String> name, ArrayList<String> rating_value, ArrayList<String> feedbacktext,ArrayList<String> date) {
            super(context,R.layout.listvisitor,R.id.nameid,name);
            this.name = name;
            this.rating_value = rating_value;
            this.feedbacktext=feedbacktext;
            this.date=date;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.listvisitor,parent,false);
            TextView n=(TextView)row.findViewById(R.id.nameid);
            RatingBar ratingBar= (RatingBar) row.findViewById(R.id.rating);
            TextView f=(TextView)row.findViewById(R.id.feedback_text);
            n.setText(name.get(position));
            ratingBar.setRating(Float.valueOf(rating_value.get(position).toString()));
            f.setText(feedbacktext.get(position));
            return row;
        }
    }

}
