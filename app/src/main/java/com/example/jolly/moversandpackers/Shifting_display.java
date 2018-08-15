package com.example.jolly.moversandpackers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.Transporter.Transporter_main;
import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shifting_display extends AppCompatActivity {

    int quotation_id,request_id,order_id;
    TextView locality_source,locality_destination,source_floor_edit,destination_floor_edit,switch_source,switch_destination, date_picker;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifting_display);
        setTitle("Shifting Details");
        locality_source=(TextView) findViewById(R.id.locality_source);
        locality_destination=(TextView)findViewById(R.id.locality_destination);
        switch_source=(TextView) findViewById(R.id.switch_source);
        switch_destination= (TextView) findViewById(R.id.switch_destination);
        source_floor_edit= (TextView) findViewById(R.id.source_edit_floor);
        destination_floor_edit= (TextView) findViewById(R.id.destination_edit_floor);
        date_picker= (TextView) findViewById(R.id.date_picker);
        Intent intent=getIntent();
        quotation_id=intent.getIntExtra("quotation_id",0);
        request_id=intent.getIntExtra("request_id",0);
        order_id=intent.getIntExtra("order_id",0);
        if (request_id>0){
            //Toast.makeText(this, ""+request_id+" " +quotation_id, Toast.LENGTH_SHORT).show();
            List<BasicNameValuePair> valuePairs=new ArrayList<>();
            valuePairs.add(new BasicNameValuePair("request_id",Integer.toString(request_id)));
            new getDetails(valuePairs,"https://packersandmovers-virali.000webhostapp.com/moversandpackers/getShiftingRequest.php").execute();
        }
        else if (quotation_id>0){
            //Toast.makeText(this, ""+request_id+" " +quotation_id, Toast.LENGTH_SHORT).show();
            List<BasicNameValuePair> valuePairs=new ArrayList<>();
            valuePairs.add(new BasicNameValuePair("quotation_id",Integer.toString(quotation_id)));
            new getDetails(valuePairs,"https://packersandmovers-virali.000webhostapp.com/moversandpackers/getShiftingQuotation.php").execute();
        }
        else if (order_id>0){
            //Toast.makeText(this, ""+request_id+" " +quotation_id, Toast.LENGTH_SHORT).show();
            List<BasicNameValuePair> valuePairs=new ArrayList<>();
            valuePairs.add(new BasicNameValuePair("shifting_id",Integer.toString(order_id)));
            new getDetails(valuePairs,"https://packersandmovers-virali.000webhostapp.com/moversandpackers/getShiftingOrder.php").execute();
        }


    }

    public class getDetails extends AsyncTask<String,String,String>{
        List<BasicNameValuePair> valuePairs=new ArrayList<>();
        String link;
        public getDetails(List<BasicNameValuePair> valuePairs, String link) {
            this.valuePairs=valuePairs;
            this.link=link;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Shifting_display.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ApiResponse response=new ApiResponse();
            response= WebInterface.doPost(link,valuePairs);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String json=s;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (json!=null){
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    int status=Integer.parseInt(jsonObject.getString("status"));
                    String Message=jsonObject.getString("Message");

                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    if (status==1) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        date_picker.setText(jsonObject1.getString("Shift_Date"));
                        if (jsonObject1.getInt("Source_Lift") == 1)
                            switch_source.setText("Yes");
                        else
                            switch_source.setText("No");
                        if (jsonObject1.getInt("Destination_Lift") == 1)
                            switch_destination.setText("Yes");
                        else
                            switch_destination.setText("No");
                        locality_source.setText(jsonObject1.getString("Source_Place"));
                        locality_destination.setText(jsonObject1.getString("Destination_Place"));
                        source_floor_edit.setText(jsonObject1.getString("Source_Floor"));
                        destination_floor_edit.setText(jsonObject1.getString("Destination_Floor"));
                    }
                    else {
                        Toast.makeText(Shifting_display.this, ""+Message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Shifting_display.this, Transporter_main.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(Shifting_display.this, "Cannot get json data", Toast.LENGTH_SHORT).show();
            }
            Log.i("data",json);
        }
    }
}
