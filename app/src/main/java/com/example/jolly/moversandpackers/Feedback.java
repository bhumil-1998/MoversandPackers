package com.example.jolly.moversandpackers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Feedback extends AppCompatActivity {
    RatingBar ratingBar;
    Button btn;
    EditText feedbacks;
    ProgressDialog progressDialog;
    float ratingvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("Feedback");
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        btn=(Button)findViewById(R.id.submit_feedback);
        feedbacks=(EditText)findViewById(R.id.feed_comment);

    }
    public void onBtnClick(View V){
        String feedback=feedbacks.getText().toString();

        ratingvalue = ratingBar.getRating();

        Toast.makeText(this,"Rating is "+ratingvalue,Toast.LENGTH_SHORT).show();
        new feedbackrating().execute(feedback);
    }
    class feedbackrating extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Feedback.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String Feedback=params[0];
           // String rating=params[2];
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("feedback",Feedback));
            nameValuePairs.add(new BasicNameValuePair("Email_id",GlobalVariable.uname));
            nameValuePairs.add(new BasicNameValuePair("value",Float.toString(ratingvalue)));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/giveFeedback.php";
            response= WebInterface.doPost(link,nameValuePairs);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            String json=s;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    Log.i("json",jsonObject.toString());
                    int status=Integer.parseInt(jsonObject.getString("status"));
                    String message=jsonObject.getString("message");
                    if(status==1){
                        Toast.makeText(Feedback.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Feedback.this,Customer_main.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Feedback.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(Feedback.this, "Cannot get json data", Toast.LENGTH_SHORT).show();
            }
            Log.i("data",json);
        }
    }
}
