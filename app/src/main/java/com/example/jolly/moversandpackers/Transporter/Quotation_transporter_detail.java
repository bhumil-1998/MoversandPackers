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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.R;
import com.example.jolly.moversandpackers.Shifting_display;
import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Quotation_transporter_detail extends AppCompatActivity {
    //String item[]={"tv","sofa","A.C","table","chair"};
    //String quantity[]={"1","2","3","4","5"};
    ArrayList<HashMap<String,String>> list;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> qty=new ArrayList<>();
    ProgressDialog progressDialog;
    ListView lv;
    int id_re,namesize;
    TextView name_txt,quotation_id,date,price;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_transporter_detail);
        setTitle("Quotation Details");
        name_txt= (TextView) findViewById(R.id.name);
        quotation_id= (TextView) findViewById(R.id.quotation_number);
        date= (TextView) findViewById(R.id.date_text);
        price= (TextView) findViewById(R.id.price);
        intent = getIntent();
        Log.i("name",intent.getStringExtra("name"));
        name_txt.setText(intent.getStringExtra("name"));
        quotation_id.setText(intent.getStringExtra("quotation_id"));
        date.setText(intent.getStringExtra("date"));
        price.setText(intent.getStringExtra("price"));

        list=new ArrayList<>();
        //price=(EditText)findViewById(R.id.price_quotation);
        id_re=intent.getIntExtra("quotation_id",0);
        Toast.makeText(this, ""+id_re, Toast.LENGTH_SHORT).show();
        lv=(ListView)findViewById(R.id.quotation_listview_transporter);
        new quotationdetail().execute();
        //new itemsdetail().execute();

    }
    public class quotationdetail extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Quotation_transporter_detail.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("quotation_id",Integer.toString(id_re)));//Integer.toString(id)
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/transporter/getItemsQuotation.php";
            response= WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            String json=s;
            Log.i("items",json);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    String message = jsonObject.getString("Message");
                    if (status==1) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("price");
                        String price_str = jsonArray1.getString(0);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String item_name = jsonObject1.getString("Name");
                            String qty_str=jsonObject1.getString("qty");
                            name.add(item_name);
                            qty.add(qty_str);
                        }
                        //price.setText(price_str);
                    }
                    else{
                        Toast.makeText(Quotation_transporter_detail.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Quotation_transporter_detail.this,Quotation_transporter.class);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Quotation_transporter_detail.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            MyAdapter adapter=new MyAdapter(getApplicationContext(),name,qty);
            lv.setAdapter(adapter);
            super.onPostExecute(s);
        }
    }
    /*public class itemsdetail extends AsyncTask<String,String,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(Quotation_transporter_detail.this);
            progressDialog.setMessage("Please Wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("quotation_id",Quotation_transporter_detail.quotation_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray=new JSONArray();
            try{
                for(int i=0;i<namesize;i++){
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("name",name.get(i));
                    jsonArray.put(jsonObject1);
                    jsonObject1.put("qty",qty.get(i));
                }
                jsonObject.put("Name",jsonArray);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("name",jsonObject.toString());
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("quotation_id",Integer.toString(id_re) ));
            ApiResponse response=WebInterface.doPost("https://packersandmovers-virali.000webhostapp.com/moversandpackers/transporter/getItemsQuotation.php",nameValuePairs);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {

            String json=s;
            Log.i("progress",json);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    String message = jsonObject.getString("Message");
                    if (status==1) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("price");
                        String price_str = jsonArray1.getString(0);
                        Log.i("price", price_str);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String item_name = jsonObject1.getString("Name");
                            String qty_name = jsonObject1.getString("qty");
                            name.add(item_name);
                            qty1.add(qty_name);
                        }
                        name_str = name.toArray(name_str);
                        qty_str = qty1.toArray(qty_str);
                        for (int i = 0; i < name_str.length; i++) {
                            Log.i("string is", (String) name_str[i]);
                            Log.i("string is", (String) qty_str[i]);
                        }
                        price.setText(price_str);
                    }
                    else {
                        Toast.makeText(Quotation_customer_detail.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Quotation_customer_detail.this,Request_customer.class);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Quotation_customer_detail.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }*/

    class MyAdapter extends ArrayAdapter {
        ArrayList<String> itemarray=new ArrayList<>();
        ArrayList<String> quantityarray=new ArrayList<>();

        public MyAdapter(Context context,  ArrayList<String> itemarray, ArrayList<String> quantityarray) {
            super(context, R.layout.activity_listview, R.id.Itemid,itemarray);
            this.itemarray = itemarray;
            this.quantityarray = quantityarray;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.activity_listview,parent,false);
            TextView item = (TextView) row.findViewById(R.id.Itemid);
            TextView quantity=(TextView)row.findViewById(R.id.Quantityid);
            item.setText(itemarray.get(position));
            quantity.setText(quantityarray.get(position));
            return row;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shifting_button,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_shifting:
                Intent intent=new Intent(this,Shifting_display.class);
                intent.putExtra("quotation_id",id_re);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
