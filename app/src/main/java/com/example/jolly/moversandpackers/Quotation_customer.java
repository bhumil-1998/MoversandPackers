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
import java.util.List;

public class Quotation_customer extends AppCompatActivity {
    String transporter_name_str;
    ProgressDialog progressDialog;
    ArrayAdapter<String> quotation_date_adapter;
    ListView quotation_listview;
    ArrayList<String> quotation_id;
    ArrayList<String> transporter_name;
    ArrayList<String> date;
    ArrayList<String> price;
    ArrayList<String> status=new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_customer);
        setTitle("Quotations");
        quotation_listview = (ListView) findViewById(R.id.listview_quotation);
        quotation_id = new ArrayList<>();
        transporter_name = new ArrayList<>();
        date = new ArrayList<>();
        price = new ArrayList<>();
        new quotation().execute();
        quotation_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_re;
                switch (position) {
                   /* case 0:
                        transporter_name_str = "bhumiltrans@.com" ;
                        break;
                    case 1:
                        transporter_name_str = "rutultrans@.com";
                        break;*/
                    default:
                        transporter_name_str = transporter_name.get(position);
                        id_re = Integer.parseInt(quotation_id.get(position));
                        break;

                }
                Intent i = new Intent(Quotation_customer.this, Quotation_customer_detail.class);
                i.putExtra("name", transporter_name.get(position));
                i.putExtra("quotation_id", quotation_id.get(position));
                i.putExtra("date",date.get(position));
                i.putExtra("price",price.get(position));
                startActivity(i);
            }

        });

    }

    public class quotation extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Quotation_customer.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            String json = s;
            /*String[] name_arr = new String[transporter_name.size()];
            String[] dateArray = new String[date.size()];
            String[] price_arr = new String[price.size()];*/
            Log.i("json",json.toString());
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (json != null) {
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    int status_str = Integer.parseInt(jsonObject.getString("status"));
                    String message = jsonObject.getString("Message");
                    if (status_str == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("0");


                            quotation_id.add(jsonObject2.getString("quotation_id"));
                            transporter_name.add(jsonObject2.getString("User_name"));
                            date.add(jsonObject2.getString("date"));
                            price.add(jsonObject2.getString("price"));
                            status.add(jsonObject2.getString("status"));
                        }

                    } else {
                        Toast.makeText(Quotation_customer.this, "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Quotation_customer.this, Customer_main.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Quotation_customer.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            quotation_date_adapter = new MyAdapter(Quotation_customer.this, transporter_name, date, price,quotation_id,status);
            quotation_listview.setAdapter(quotation_date_adapter);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            ApiResponse response = new ApiResponse();
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("email_id", GlobalVariable.uname));
            String link = "https://packersandmovers-virali.000webhostapp.com/moversandpackers/getQuotationTransName.php";
            response = WebInterface.doPost(link, nameValuePairs);
            String Json = WebInterface.getData(link);
            return response.response;
        }
    }


    class MyAdapter extends ArrayAdapter {
        //String[] dateArray,source_arr,destination_arr,status;
        ArrayList<String> date=new ArrayList<>();
        ArrayList<String> transporter_name=new ArrayList<>();
        ArrayList<String> price=new ArrayList<>();
        ArrayList<String> quotation_id=new ArrayList<>();
        ArrayList<String> status=new ArrayList<>();



        public MyAdapter(Context context, ArrayList<String> title, ArrayList title2,ArrayList title3,ArrayList title4,ArrayList title5){
            super(context, R.layout.list_quotation, R.id.date_text_view,title);
            this.date=title;
            this.transporter_name=title2;
            this.price=title3;
            quotation_id=title4;
            status=title5;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.list_quotation,parent,false);
            TextView date = (TextView) row.findViewById(R.id.date_text_view);
            TextView trans_name=(TextView)row.findViewById(R.id.trans_name_text_view);
            TextView price=(TextView)row.findViewById(R.id.price_text);
            TextView quotation_number=(TextView)row.findViewById(R.id.quotation_number_text_cust);
            TextView status_txt=(TextView)row.findViewById(R.id.status);

            date.setText(this.date.get(position));
            trans_name.setText(this.transporter_name.get(position));
            price.setText(this.price.get(position));
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
