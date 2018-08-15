package com.example.jolly.moversandpackers;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.id;

public class Quotation_customer_detail extends AppCompatActivity {
   // String item[] = {"TV", "chair", "furniture", "bad", "table", "AC"};
   // String quantity[] = {"2", "3", "4", "5", "1", "7"};
    ListView lv;
    ArrayList<HashMap<String,String>> list;
    ProgressDialog progressDialog;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> qty1=new ArrayList<>();
    Button placeorder;
    TextView name_txt,date,number,price_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_customer_detail);
        setTitle("Quotation Detail");
        Intent intent = getIntent();
        //id=intent.getStringExtra("quotation_id");
        placeorder= (Button) findViewById(R.id.Placeorder);
        name_txt= (TextView) findViewById(R.id.transportertext);
        date= (TextView) findViewById(R.id.date_text);
        number= (TextView) findViewById(R.id.quotation_number);
        price_txt= (TextView) findViewById(R.id.price);

        name_txt.setText(intent.getStringExtra("name"));
        date.setText(intent.getStringExtra("date"));
        number.setText(intent.getStringExtra("quotation_id"));
        Log.i("price",intent.getStringExtra("price"));
        price_txt.setText(intent.getStringExtra("price"));
        lv = (ListView) findViewById(R.id.item_quotation_listview);
        list=new ArrayList<>();
        new getquotation().execute();

        /*MyAdapterquantity adapter = new MyAdapterquantity(getApplicationContext(), item, quantity);
        lv.setAdapter(adapter);*/
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new giveorder().execute();
                }
        });
    }


    public class getquotation extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Quotation_customer_detail.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("quotation_id",Integer.toString(id)));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/transporter/getItemsQuotation.php";
            response= WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }
        @Override
        protected void onPostExecute(String s) {
            String json=s;
            String[] name_str=new String[name.size()];
            String[] qty_str=new String[qty1.size()];

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
            MyAdapter adapter=new MyAdapter(getApplicationContext(),name_str,qty_str);
            lv.setAdapter(adapter);

            super.onPostExecute(s);
        }
    }
    public class giveorder extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Quotation_customer_detail.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("quotation_id",Integer.toString(id)));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/giveOrder.php";
            response= WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }
        @Override
        protected void onPostExecute(String s) {
            String json=s;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    String message = jsonObject.getString("message");
                    if (status==1) {
                        Toast.makeText(Quotation_customer_detail.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Quotation_customer_detail.this,Customer_main.class);
                        startActivity(intent);
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
    }
    class MyAdapter  extends ArrayAdapter {
        String[] itemarray;
        String[] QuantityArray;

        public MyAdapter(Context context, String[] title, String[] title2) {
            super(context, R.layout.activity_listview, R.id.Itemid, title);
            this.itemarray = title;
            this.QuantityArray = title2;
        }

        @NonNull

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.activity_listview, parent, false);
            TextView item = (TextView) row.findViewById(R.id.Itemid);
            TextView quantity = (TextView) row.findViewById(R.id.Quantityid);
            item.setText(itemarray[position]);
            quantity.setText(QuantityArray[position]);
            return row;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shifting_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_shifting:
                Intent intent = new Intent(this, Shifting_display.class);
                intent.putExtra("quotation_id",id);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}