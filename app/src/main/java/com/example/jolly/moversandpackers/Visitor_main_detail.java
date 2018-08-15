package com.example.jolly.moversandpackers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Visitor_main_detail extends AppCompatActivity {

    RatingBar ratingBar;
    TextView date,feedback;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Visitor Details");
        setContentView(R.layout.activity_visitor_main_detail);
        ratingBar= (RatingBar) findViewById(R.id.rating_visitor);
        date= (TextView) findViewById(R.id.datevisitor);
        feedback= (TextView) findViewById(R.id.feedback_text);
        Intent intent=getIntent();
        ratingBar.setRating(Float.valueOf(intent.getStringExtra("rating_value")));
        date.setText(intent.getStringExtra("date"));
        feedback.setText(intent.getStringExtra("feedback"));
        setTitle(intent.getStringExtra("name"));

    }

}
