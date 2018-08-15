package com.example.jolly.moversandpackers.Transporter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.GlobalVariable;
import com.example.jolly.moversandpackers.R;
import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Request_transporter extends AppCompatActivity {
    String request_date_str;
    EditText price;
    ArrayAdapter<String> request_date_adapter;

    ArrayList<String> request,customer,source,destination,status,date;
    ListView request_listview;
    int id_re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_transporter);
        setTitle("Requests");

        price= (EditText) findViewById(R.id.request_price);
        request_listview = (ListView)findViewById(R.id.listview_request_transporter);
        request=new ArrayList<>();
        customer=new ArrayList<>();
        source=new ArrayList<>();
        destination=new ArrayList<>();
        status=new ArrayList<>();
        date=new ArrayList<>();
        new getName().execute();

        request_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    default:
                        request_date_str = customer.get(position);
                        id_re=Integer.parseInt(request.get(position));
                        //Toast.makeText(Request_customer.this, ""+request_date_str+id_re, Toast.LENGTH_SHORT).show();
                        break;

                }
                Toast.makeText(Request_transporter.this, ""+id_re, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Request_transporter.this,Request_transporter_detail.class);
                intent.putExtra("customer_name",customer.get(position));
                intent.putExtra("request_id",request.get(position));
                intent.putExtra("source",source.get(position));
                intent.putExtra("destination",destination.get(position));
                intent.putExtra("date",date.get(position));
                startActivity(intent);

            }
        });

    }
    class Myadapter extends ArrayAdapter{
        ArrayList<String> req_id=new ArrayList<>();
        ArrayList<String> user_name=new ArrayList<>();
        ArrayList<String> source_place1=new ArrayList<>();
        ArrayList<String> destination_place1=new ArrayList<>();
        ArrayList<String> status=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();

        public Myadapter(Context context,ArrayList<String> req_id, ArrayList<String> user_name, ArrayList<String> source_place1, ArrayList<String> destination_place1,ArrayList<String> status,ArrayList<String> date) {
            super(context, R.layout.listrequest_transporter, R.id.username_trans,req_id);
            this.req_id = req_id;
            this.user_name = user_name;
            this.source_place1 = source_place1;
            this.destination_place1 = destination_place1;
            this.status=status;
            this.date=date;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.listrequest_transporter,parent,false);
            TextView user_name1=(TextView)row.findViewById(R.id.username_trans);
            TextView source_place=(TextView)row.findViewById(R.id.source_place_text_trans);
            TextView destination_place=(TextView)row.findViewById(R.id.destination_place_text_trans);
            TextView status_txt= (TextView) row.findViewById(R.id.status);
            TextView date= (TextView) row.findViewById(R.id.date_text_request_trans);
            user_name1.setText(user_name.get(position));
            source_place.setText(source_place1.get(position));
            destination_place.setText(destination_place1.get(position));
            if (Integer.parseInt(this.status.get(position))==0){
                status_txt.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                status_txt.setText("Pending");
            }
            else if (Integer.parseInt(this.status.get(position))==1){
                status_txt.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                status_txt.setText("Quotation Issued");
            }
            else if (Integer.parseInt(this.status.get(position))==2){
                status_txt.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                status_txt.setText("Ordered");
            }
            date.setText(this.date.get(position));
            //row.setBackgroundColor(getResources().getColor(R.color.row));
            return row;
        }
    }

    public class getName extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(Request_transporter.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Email_id", GlobalVariable.uname));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/transporter/getRequestCustomer.php";
            ApiResponse response= WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String json=s;
            Log.i("json",json);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (json!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);

                    int status_str=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    if (status_str==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("response");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            request.add(jsonObject1.getString("request_id"));
                            customer.add(jsonObject1.getString("User_name"));
                            source.add(jsonObject1.getString("Source_Place"));
                            destination.add(jsonObject1.getString("Destination_Place"));
                            status.add(jsonObject1.getString("status"));
                            date.add(jsonObject1.getString("Request_Date"));
                        }
                        request_date_adapter = new Myadapter(Request_transporter.this,request,customer,source,destination,status,date);
                        request_listview.setAdapter(request_date_adapter);
                    }
                    else{
                        Toast.makeText(Request_transporter.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Request_transporter.this,Transporter_main.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               }
            else{
                Toast.makeText(Request_transporter.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
