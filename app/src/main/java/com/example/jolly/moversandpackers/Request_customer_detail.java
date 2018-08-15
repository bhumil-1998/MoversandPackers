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

public class Request_customer_detail extends AppCompatActivity {
    /*String item[] = {"TV", "chair", "furniture", "bad", "table", "AC"};
    String quantity[]={"2","3","4","5","1","7"};*/
    ListView lv;
    ArrayList<HashMap<String,String>> list;
    ProgressDialog progressDialog;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<String> quant=new ArrayList<>();
    TextView to,from,date;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_customer_detail);
        Intent intent=getIntent();
        setTitle("Request Details");
        to= (TextView) findViewById(R.id.destination_place_text);
        from= (TextView) findViewById(R.id.source_place_text);
        date= (TextView) findViewById(R.id.date_text_request);

        to.setText(intent.getStringExtra("source"));
        from.setText(intent.getStringExtra("destination"));
        date.setText(intent.getStringExtra("date"));
        id=intent.getIntExtra("request_id",0);
        lv=(ListView)findViewById(R.id.item_request_listview);
        //MyAdapter adapter=new MyAdapter(getApplicationContext(),item,quantity);
        //lv.setAdapter(adapter);
        list=new ArrayList<>();
        new getitem().execute();
    }

    public class getitem extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Request_customer_detail.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("request_id",Integer.toString(id)));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/getItemsRequest.php";
            response= WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            String json=s;
            String[] name_str=new String[items.size()];
            String[] qty_str=new String[quant.size()];
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    int status=Integer.parseInt(jsonObject.getString("status"));
                    String message=jsonObject.getString("Message");
                    if (status==1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String item_name = jsonObject1.getString("Name");
                            String qty = jsonObject1.getString("qty");
                        /*HashMap<String,String> Myadapter=new HashMap<>();
                        Myadapter.put("Items",item_name);
                        Myadapter.put("Quantity",qty);
                        list.add(Myadapter);*/
                            items.add(item_name);
                            quant.add(qty);
                        }

                        name_str = items.toArray(name_str);
                        qty_str = quant.toArray(qty_str);
                        for (int i = 0; i < name_str.length; i++) {
                            Log.d("string is", (String) name_str[i]);
                            Log.d("string is", (String) qty_str[i]);
                        }
                        //Log.i("name",items.toString());
                        //Log.i("qty",quant.toString());
                    }
                    else {
                        Toast.makeText(Request_customer_detail.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Request_customer_detail.this,Request_customer.class);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Request_customer_detail.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            MyAdapter adapter=new MyAdapter(getApplicationContext(),name_str,qty_str);
            lv.setAdapter(adapter);
            super.onPostExecute(s);

        }
    }

    class MyAdapter extends ArrayAdapter {
        String[] itemarray;
        String[] QuantityArray;

        public MyAdapter(Context context, String[]title, String[] title2){
            super(context, R.layout.activity_listview, R.id.Itemid,title);
            this.itemarray=title;
            this.QuantityArray=title2;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.activity_listview,parent,false);
            TextView item = (TextView) row.findViewById(R.id.Itemid);
            TextView quantity=(TextView)row.findViewById(R.id.Quantityid);
            //Log.i("name",nameList.get(position).toString());
            //item.setText(nameList.get(position).toString());
            //quantity.setText(qtyList.get(position).toString());
            item.setText(itemarray[position]);
            quantity.setText(QuantityArray[position]);
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
                intent.putExtra("request_id",id);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}