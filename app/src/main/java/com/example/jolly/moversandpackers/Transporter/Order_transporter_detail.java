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

public class Order_transporter_detail extends AppCompatActivity {
    //String item[]={"tv","sofa","A.C","table","chair","Microwave","box"};
    //String quantity[]={"1","2","3","4","5","6","7"};
    ListView lv;
    ArrayList<HashMap<String,String>> list;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> qty1=new ArrayList<>();
    int id;
    ProgressDialog progressDialog;
    TextView name_txt,order_id,quotation_id,order_date,price;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_transporter_detail);
        setTitle("Order Detail");
        name_txt= (TextView) findViewById(R.id.transportertext);
        order_id= (TextView) findViewById(R.id.order_id);
        quotation_id= (TextView) findViewById(R.id.quotation_number_order);
        order_date= (TextView) findViewById(R.id.date_text);
        price= (TextView) findViewById(R.id.order_price);
        intent = getIntent();
        name_txt.setText(intent.getStringExtra("name"));
        order_id.setText(intent.getStringExtra("shifting_id"));
        quotation_id.setText(intent.getStringExtra("quotation_id"));
        order_date.setText(intent.getStringExtra("date"));
        price.setText(intent.getStringExtra("price"));

        new ordertransporterdetail().execute();
        lv=(ListView)findViewById(R.id.order_detail_transporter);

        list=new ArrayList<>();

      /*  MyAdapter adapter=new MyAdapter(getApplicationContext(),item,quantity);
        lv.setAdapter(adapter);*/
    }
    public class ordertransporterdetail extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Order_transporter_detail.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("shifting_id",intent.getStringExtra("shifting_id")));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/getOrderDetail.php";
            response= WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }
        @Override
        protected void onPostExecute(String s) {
            String json=s;
            String[] name_str=new String[name.size()];
            String[] qty_str=new String[qty1.size()];
            Log.i("json",json);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    String message = jsonObject.getString("Message");
                    if (status==1) {
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
                    else{
                        Toast.makeText(Order_transporter_detail.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Order_transporter_detail.this,Order_transporter.class);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Order_transporter_detail.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            MyAdapter adapter=new MyAdapter(getApplicationContext(),name_str,qty_str);
            lv.setAdapter(adapter);
            super.onPostExecute(s);
        }
    }
    class MyAdapter extends ArrayAdapter {
        String itemarray[];
        String quantityarray[];

        public MyAdapter(Context context, String[] itemarray, String[] quantityarray) {
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
            item.setText(itemarray[position]);
            quantity.setText(quantityarray[position]);
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
                intent.putExtra("order_id",intent.getStringExtra("shifting_id"));
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
