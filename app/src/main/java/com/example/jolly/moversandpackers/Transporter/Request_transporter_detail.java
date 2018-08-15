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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.GlobalVariable;
import com.example.jolly.moversandpackers.R;
import com.example.jolly.moversandpackers.Shifting_display;
import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Request_transporter_detail extends AppCompatActivity {
    /*String item[]={"tv","sofa","A.C","table","chair","Microwave","box"};
    String quantity[]={"1","2","3","4","5","6","7"};*/
    ListView lv;
    ProgressDialog progressDialog;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<String> quant=new ArrayList<>();
    Button submit_quotation;
    EditText price;
    int id_re,namesize;
    TextView name_txt,to,date,from;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_transporter_detail);
        setTitle("Request Details");
        name_txt= (TextView) findViewById(R.id.username);
        to= (TextView) findViewById(R.id.source_place_text_trans);
        from= (TextView) findViewById(R.id.destination_place_text_trans);
        date= (TextView) findViewById(R.id.date_text_request_trans);


        intent = getIntent();
        name_txt.setText(intent.getStringExtra("customer_name"));
        to.setText(intent.getStringExtra("source"));
        date.setText(intent.getStringExtra("date"));
        from.setText(intent.getStringExtra("destination"));

        submit_quotation= (Button) findViewById(R.id.submit_quotation);
        price= (EditText) findViewById(R.id.request_price);
        id_re = intent.getIntExtra("request_id",0);
        lv=(ListView)findViewById(R.id.request_listview_transporter);
        new getitem().execute();
        submit_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price_str=price.getText().toString();
                if(price_str.length()>0) {
                    new givequotation().execute(price_str, Integer.toString(id_re));
                }
                else{
                    price.setError("invalid price");
                }
            }
        });
    }

    public class getitem extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Request_transporter_detail.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("request_id",intent.getStringExtra("request_id")));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/getItemsRequest.php";
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
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    int status=Integer.parseInt(jsonObject.getString("status"));
                    String message=jsonObject.getString("Message");
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String item_name=jsonObject1.getString("Name");
                        String qty=jsonObject1.getString("qty");
                        /*HashMap<String,String> Myadapter=new HashMap<>();
                        Myadapter.put("Items",item_name);
                        Myadapter.put("Quantity",qty);
                        list.add(Myadapter);*/
                        items.add(item_name);
                        quant.add(qty);
                    }

                    //Log.i("name",items.toString());
                    //Log.i("qty",quant.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            MyAdapter adapter=new MyAdapter(getApplicationContext(),items,quant);
            lv.setAdapter(adapter);
            super.onPostExecute(s);

        }
    }

    public class givequotation extends  AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Request_transporter_detail.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Email_id", GlobalVariable.uname));
            nameValuePairs.add(new BasicNameValuePair("price",params[0]));
            nameValuePairs.add(new BasicNameValuePair("request_id",params[1]));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/transporter/giveQuotation.php";
            ApiResponse response= WebInterface.doPost(link,nameValuePairs);
            //sending items to item_quotation
            /*JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("quotation_id",Quotation_transporter_detail.quotation_id);
                JSONArray jsonArray=new JSONArray();

                for(int i=0;i<namesize;i++){
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("name",items.get(i));
                    jsonArray.put(jsonObject1);
                    jsonObject1.put("qty",quant.get(i));
                }
                jsonObject.put("Name",jsonArray);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("name",jsonObject.toString());
            WebInterface.doWS("https://packersandmovers-virali.000webhostapp.com/moversandpackers/giveQuotationItems",jsonObject.toString());*/
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String json=s;
            Log.i("date",json.toString());
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (json!=null){
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    int status=jsonObject.getInt("status");
                    String message=jsonObject.getString("message");
                    if (status==1){
                        Toast.makeText(Request_transporter_detail.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Request_transporter_detail.this,Transporter_main.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Request_transporter_detail.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
                //intent.putExtra("xyz",false);
                intent.putExtra("request_id",id_re);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    class MyAdapter extends ArrayAdapter{
        ArrayList<String> itemList=new ArrayList<>();
        ArrayList<String> quantityList=new ArrayList<>();

        public MyAdapter(Context context,ArrayList<String> item, ArrayList<String> quantity) {
            super(context, R.layout.activity_listview, R.id.Itemid,item);
            this.itemList = item;
            this.quantityList = quantity;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.activity_listview,parent,false);
            TextView item = (TextView) row.findViewById(R.id.Itemid);
            TextView quantity=(TextView)row.findViewById(R.id.Quantityid);
            item.setText(itemList.get(position));
            quantity.setText(quantityList.get(position));
            return row;
        }
    }
}
