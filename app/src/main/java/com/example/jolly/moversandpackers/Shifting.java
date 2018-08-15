package com.example.jolly.moversandpackers;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;
import com.example.jolly.moversandpackers.fragments.item;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Shifting extends AppCompatActivity {

    private DatePicker dpResult;
    Button submit_btn;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    Switch aSwitch,aSwitch2;
    TextView datetmp;
    int floor_source=0,floor_destination=0;
    EditText source_floor_edit,destination_floor_edit;
    Button date_picker;
    EditText locality_source,locality_destination;
    boolean check=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifting);
        Intent intent=getIntent();
        String get=intent.getStringExtra("xyz");
        /*if (get=="false"){
            check=false;
            editfalse(check);
        }
        else {
            check = true;
            editfalse(check);
        }*/
        datetmp=(TextView) findViewById(R.id.tmp);
        submit_btn=(Button)findViewById(R.id.submit);
        locality_source=(EditText)findViewById(R.id.locality_source);
        locality_destination=(EditText)findViewById(R.id.locality_destination);
        aSwitch=(Switch)findViewById(R.id.switch_source);
        aSwitch2= (Switch) findViewById(R.id.switch_destination);
        source_floor_edit= (EditText) findViewById(R.id.source_edit_floor);
        destination_floor_edit= (EditText) findViewById(R.id.destination_edit_floor);
        date_picker= (Button) findViewById(R.id.date_picker);
        /*aSwitch2=(Switch)findViewById(R.id.switch_destination);
        source_floor_edit= (EditText) findViewById(R.id.source_edit_floor);
        destination_floor_edit= (EditText) findViewById(R.id.destination_edit_floor);*/
        /*Date date=Calendar.getInstance().getTime();
        String date_str=date.toString();
        date_picker.setText(date_str);*/
        addListenerOnButton();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    CharSequence date=date_picker.getText();

                    int source_switch=0,destination_switch=0;
                    String locality_s = locality_source.getText().toString().trim();
                    String locality_d = locality_destination.getText().toString().trim();
                    if (locality_s.isEmpty() && locality_s.length() <= 0) {
                        locality_source.setError("enter locality");
                    }
                    if (locality_d.isEmpty() && locality_d.length() <= 0) {
                        locality_destination.setError("enter locality");
                    }
                    if(datetmp.getText().toString().equals("")){
                        //Toast.makeText(Shifting.this, "Select date", Toast.LENGTH_SHORT).show();
                        date_picker.setError("Select date");
                    }
                    else {
                        Toast.makeText(Shifting.this, "ok", Toast.LENGTH_SHORT).show();
                        if (aSwitch.isChecked())
                            source_switch=1;
                        else if (!aSwitch.isChecked())
                            source_switch=0;
                        if (aSwitch2.isChecked())
                            destination_switch=1;
                        else if (!aSwitch2.isChecked())
                            destination_switch=0;
                        new shiftasync(date,locality_source.getText().toString().trim(),locality_destination.getText().toString().trim(),source_switch,destination_switch,source_floor_edit.getText().toString().trim(),destination_floor_edit.getText().toString().trim()).execute();

                    }

            }
        });
    }

    public void addListenerOnButton() {
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        day,month,year);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String date=day+"/"+month+"/"+year;
            date_picker.setText(date);
            datetmp.setText(date);
        }
    };
    public void onSwitchClick(View v){
        if(aSwitch.isChecked())
            Toast.makeText(Shifting.this,"Available",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Shifting.this,"Not Available",Toast.LENGTH_SHORT).show();
    }
    public void onSwitchClicker(View v){
        if(aSwitch2.isChecked())
            Toast.makeText(Shifting.this,"Available",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Shifting.this,"Not Available",Toast.LENGTH_SHORT).show();
    }

    public void increment_source(View View) {
        floor_source++;
        String asd=Integer.toString(floor_source);
        source_floor_edit.setText(asd);
    }

    public void decrement_source(View View) {
        if (floor_source > 0) {
            floor_source--;
            source_floor_edit.setText(Integer.toString(floor_source));
        } else
            Toast.makeText(getApplicationContext(), "Source floor cannot be less than 0", Toast.LENGTH_SHORT).show();
    }
    public void increment_destination(View View) {
        floor_destination++;
        destination_floor_edit.setText(Integer.toString(floor_destination));
    }

    public void decrement_destination(View View) {
        if (floor_destination > 0) {
            floor_destination--;
            destination_floor_edit.setText(Integer.toString(floor_destination));
        } else
            Toast.makeText(getApplicationContext(), "Destination floor cannot be less than 0", Toast.LENGTH_SHORT).show();
    }
    public void editfalse(boolean ans){
       destination_floor_edit.setEnabled(ans);
        source_floor_edit.setEnabled(ans);
        locality_source.setEnabled(ans);
        locality_destination.setEnabled(ans);
    }
    public class shiftasync extends AsyncTask<String,String,String> {
        CharSequence date;
        String locality_source,locality_destination,source_floor,destination_floor;
        int source_switch,destination_switch;
        ProgressDialog progressDialog;

        public shiftasync(CharSequence date, String locality_source, String locality_destination, int source_switch, int destination_switch,String source_floor, String destination_floor) {
            this.date = date;
            this.locality_source = locality_source;
            this.locality_destination = locality_destination;
            this.source_floor = source_floor;
            this.destination_floor = destination_floor;
            this.source_switch = source_switch;
            this.destination_switch = destination_switch;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Shifting.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Email_id",GlobalVariable.uname));
            nameValuePairs.add(new BasicNameValuePair("shift_date",date.toString()));
            nameValuePairs.add(new BasicNameValuePair("source_lift",Integer.toString(source_switch)));
            nameValuePairs.add(new BasicNameValuePair("destination_lift",Integer.toString(destination_switch)));
            nameValuePairs.add(new BasicNameValuePair("source_place",locality_source));
            nameValuePairs.add(new BasicNameValuePair("destination_place",locality_destination));
            nameValuePairs.add(new BasicNameValuePair("source_floor",source_floor));
            nameValuePairs.add(new BasicNameValuePair("destination_floor",destination_floor));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/giveShiftingDetail.php";
            ApiResponse response= WebInterface.doPost(link,nameValuePairs);
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
                    int status=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    int last_insert_id=jsonObject.getInt("last_insert_id");
                    if (status==1){
                        Intent intent = new Intent(Shifting.this, item.class);
                        intent.putExtra("request_id",last_insert_id);
                        Log.i("last insert id",Integer.toString(last_insert_id));
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Shifting.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Shifting.this, "Cannot get Data from server", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
