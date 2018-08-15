package com.example.jolly.moversandpackers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


public class Order_customer extends AppCompatActivity {
    String order;
    ListView order_listview;
    ArrayList<HashMap<String,String>> list;
    ProgressDialog progressDialog;
    ArrayAdapter<String> order_adapter;
    ArrayList<String> Shifting_id;
    ArrayList<String> Date;
    ArrayList<String> price;
    ArrayList<String> transporter_name;
    ArrayList<String> quotation_id=new ArrayList<>();
    ArrayList<String> status=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_customer);
        setTitle("Orders");
        list=new ArrayList<>();
        ArrayAdapter<String> order_adapter;
        order_listview=(ListView)findViewById(R.id.listview_order);
        transporter_name = new ArrayList<>();
        Shifting_id = new ArrayList<>();
        Date = new ArrayList<>();
        price = new ArrayList<>();
        new order().execute();
        order_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int order_id;
                switch (position){
                    default:
                        order = Date.get(position);
                        order_id=Integer.parseInt(Shifting_id.get(position));
                        break;
                }
                Intent i=new Intent(Order_customer.this,Order_detail.class);
                i.putExtra("order",order);
                i.putExtra("shifting_id",Shifting_id.get(position));
                i.putExtra("transporter_name",transporter_name.get(position));
                i.putExtra("quotation_id",quotation_id.get(position));
                i.putExtra("price",price.get(position));
                i.putExtra("status",status.get(position));
                i.putExtra("date",Date.get(position));
                startActivity(i);
            }

        });
}


    public class order extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Order_customer.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Email_id", GlobalVariable.uname));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/getOrder.php";
            response = WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {

            String json=s;
           /* String[] name_str=new String[shifting_id.size()];
            String[] qty_str=new String[shifting_date.size()];*/
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    Log.i("json",jsonObject.toString());
                    int status_str=Integer.parseInt(jsonObject.getString("status"));
                    String message=jsonObject.getString("Message");
                    if (status_str==1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("0");
                            Shifting_id.add(jsonObject1.getString("Shifting_id"));
                            Date.add(jsonObject1.getString("Date"));
                            price.add(jsonObject1.getString("price"));
                            transporter_name.add( jsonObject2.getString("User_name"));
                            quotation_id.add(jsonObject1.getString("quotation_id"));
                            status.add(jsonObject1.getString("status"));
                        }

                    }
                    else {
                        Toast.makeText(Order_customer.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Order_customer.this,Request_customer.class);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Order_customer.this,"cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            order_adapter= new Myadapter(Order_customer.this,Shifting_id,Date,price,transporter_name,quotation_id,status);
            order_listview.setAdapter(order_adapter);
            super.onPostExecute(s);
        }
    }
    class Myadapter extends ArrayAdapter{
        ArrayList<String > order_id=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();
        ArrayList<String> price=new ArrayList<>();
        ArrayList<String> transporter_name=new ArrayList<>();
        ArrayList<String> quotation_id=new ArrayList<>();
        ArrayList<String> status=new ArrayList<>();

        public Myadapter(Context context,ArrayList<String> order_id, ArrayList<String> date, ArrayList<String> price, ArrayList<String> transporter_name,ArrayList<String> quotation_id, ArrayList<String> status) {
            super(context, R.layout.listorder, R.id.date_text,order_id);
            this.order_id = order_id;
            this.date = date;
            this.price = price;
            this.transporter_name = transporter_name;
            this.quotation_id = quotation_id;
            this.status = status;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.listorder,parent,false);
            TextView order=(TextView)row.findViewById(R.id.order_id);
            TextView date = (TextView) row.findViewById(R.id.date_text);
            // TextView prices=(TextView)row.findViewById(R.id.pricetext);
            TextView trans_name=(TextView)row.findViewById(R.id.transportertext);
            TextView quotation_number=(TextView)row.findViewById(R.id.quotation_number_order);
            TextView status_txt=(TextView)row.findViewById(R.id.status);
            order.setText(order_id.get(position));
            date.setText(this.date.get(position));
            //prices.setText(price.get(position));
            trans_name.setText(transporter_name.get(position));
            quotation_number.setText(quotation_id.get(position));
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
            return row;
        }
    }
}
