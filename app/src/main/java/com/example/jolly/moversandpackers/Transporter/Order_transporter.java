package com.example.jolly.moversandpackers.Transporter;

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

public class Order_transporter extends AppCompatActivity {
    String order_str;
    ListView order_listview;
    ArrayList<String> shifting_id=new ArrayList<>();
    ArrayList<String> shifting_date=new ArrayList<>();
    ArrayList<String> quotation_id=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> status=new ArrayList<>();
    ArrayList<String> price=new ArrayList<>();
    ProgressDialog progressDialog;
    ArrayAdapter<String> order_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_transporter);
        setTitle("Orders");
        order_listview=(ListView)findViewById(R.id.listview_order_transporter);
        new ordertransporter().execute();
        order_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int order_id;
                switch (position){

                    default:
                        order_str = shifting_date.get(position);
                        order_id=Integer.parseInt(shifting_id.get(position));
                        break;
                }
                Intent i=new Intent(Order_transporter.this,Order_transporter_detail.class);
                i.putExtra("shifting_id",shifting_id.get(position));
                i.putExtra("name",name.get(position));
                i.putExtra("quotation_id",quotation_id.get(position));
                i.putExtra("date",shifting_date.get(position));
                i.putExtra("price",price.get(position));
                startActivity(i);
            }
        });
    }
    public class ordertransporter extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Order_transporter.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

            String json=s;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    Log.i("msg",jsonObject.toString());
                    int status_txt=Integer.parseInt(jsonObject.getString("status"));
                    String message=jsonObject.getString("Message");
                    if (status_txt==1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("0");
                            shifting_id.add(jsonObject1.getString("Shifting_id"));
                            shifting_date.add(jsonObject1.getString("Date"));
                            quotation_id.add(jsonObject1.getString("quotation_id"));
                            price.add(jsonObject1.getString("price"));
                            name.add(jsonObject2.getString("User_name"));
                            status.add(jsonObject2.getString("status"));
                        }

                    }
                    else{
                        Toast.makeText(Order_transporter.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Order_transporter.this,Transporter_main.class);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Order_transporter.this,"cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            order_adapter= new Myadapter(Order_transporter.this,shifting_id,shifting_date,quotation_id,name,status);
            order_listview.setAdapter(order_adapter);
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... params) {

            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Email_id", GlobalVariable.uname));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/transporter/getOrder.php";
            response = WebInterface.doPost(link,nameValuePairs);
            String json= WebInterface.getData(link);
            return response.response;
        }
    }
    class Myadapter extends ArrayAdapter{
        ArrayList<String > order_id=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();
        ArrayList<String> quotation_id=new ArrayList<>();
        ArrayList<String> name=new ArrayList<>();
        ArrayList<String> status=new ArrayList<>();

        public Myadapter(Context context, ArrayList<String> order_id, ArrayList<String> date, ArrayList<String> quotation_id, ArrayList<String> name,ArrayList<String> status) {
            super(context, R.layout.list_order_transporter, R.id.date_text,order_id);
            this.order_id = order_id;
            this.date = date;
            this.quotation_id = quotation_id;
            this.name = name;
            this.status=status;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.list_order_transporter,parent,false);
            TextView order=(TextView)row.findViewById(R.id.order_id_text_view);
            TextView date = (TextView) row.findViewById(R.id.date_text_view);
            TextView quotation_number=(TextView)row.findViewById(R.id.quotation_number_order);
            TextView trans_name=(TextView)row.findViewById(R.id.user_order);
            TextView status_txt=(TextView)row.findViewById(R.id.status);
            order.setText(order_id.get(position));
            date.setText(this.date.get(position));
            quotation_number.setText(quotation_id.get(position));
            trans_name.setText(name.get(position));
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
