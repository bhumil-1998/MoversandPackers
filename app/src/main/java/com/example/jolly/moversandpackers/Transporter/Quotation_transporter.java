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

public class Quotation_transporter extends AppCompatActivity {
    String transporter_name_str;
    ArrayList<String> quotation_id=new ArrayList<>();
    ArrayList<String> user=new ArrayList<>();
    ArrayList<String> price1=new ArrayList<>();
    ArrayList<String> quotation_date=new ArrayList<>();
    ArrayList<String> status=new ArrayList<>();
    ListView quotation_listview;
    ProgressDialog progressDialog;
    ArrayAdapter<String> quotation_date_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_transporter);
        setTitle("Quotations");

        quotation_listview = (ListView) findViewById(R.id.listview_quotation_transporter);
        new quotation().execute();
        quotation_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_re;
                switch (position) {
                    /*case 0:
                        transporter_name_str = "satyam";
                        break;
                    case 1:
                        transporter_name_str = "swy";
                        break;
                    case 2:
                        transporter_name_str = "hfdk";
                        break;
                    case 3:
                        transporter_name_str = "hww";
                        break;*/
                    default:
                        transporter_name_str=quotation_date.get(position);
                        id_re=Integer.parseInt(quotation_id.get(position));
                        break;


                }
                Intent i=new Intent(Quotation_transporter.this,Quotation_transporter_detail.class);
                i.putExtra("name",user.get(position));
                i.putExtra("quotation_id",quotation_id.get(position));
                i.putExtra("date",quotation_date.get(position));
                i.putExtra("price",price1.get(position));
                startActivity(i);
            }
        });
    }

    public class quotation extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Quotation_transporter.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Email_id", GlobalVariable.uname));
            String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/transporter/getQuotationDate.php";
            response= WebInterface.doPost(link,nameValuePairs);
            String Json= WebInterface.getData(link);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            String json=s;
            Log.i("data",json);

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if(json!=null){
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    int status_str=Integer.parseInt(jsonObject.getString("status"));
                    String message=jsonObject.getString("Message");
                    if (status_str==1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject2=jsonObject1.getJSONObject("0");
                            quotation_id.add(jsonObject1.getString("quotation_id"));
                            quotation_date.add(jsonObject1.getString("Date"));
                            user.add(jsonObject2.getString("User_name"));
                            status.add(jsonObject2.getString("status"));
                            price1.add(jsonObject1.getString("price"));

                        }
                    }
                    else if (status_str==0){
                        Toast.makeText(Quotation_transporter.this, ""+message, Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Quotation_transporter.this,Transporter_main.class);
                        startActivity(intent);
                    }
                    Log.i("name",quotation_date.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(Quotation_transporter.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
            }
            quotation_date_adapter = new Myadapter(Quotation_transporter.this,quotation_id,user,quotation_date,price1,status);
            quotation_listview.setAdapter(quotation_date_adapter);
            super.onPostExecute(s);
        }


    }
class Myadapter extends ArrayAdapter{
    ArrayList<String> user=new ArrayList<>();
    ArrayList<String> quo_date=new ArrayList<>();
    ArrayList<String> prices=new ArrayList<>();
    ArrayList<String> quotation_id=new ArrayList<>();
    ArrayList<String> status=new ArrayList<>();

    public Myadapter(Context context,ArrayList<String> quotation_id,ArrayList<String> user, ArrayList<String> quo_date, ArrayList<String> prices,ArrayList<String> status) {
        super(context, R.layout.listquotation_transporter, R.id.user_quotation,user);
        this.quotation_id=quotation_id;
        this.user = user;
        this.quo_date = quo_date;
        this.prices = prices;
        this.status=status;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.listquotation_transporter,parent,false);
        TextView quotation_id=(TextView)row.findViewById(R.id.quotation_number_text_trans);
        TextView quotation=(TextView)row.findViewById(R.id.user_quotation);
        TextView date=(TextView)row.findViewById(R.id.date_text_quotation);
        TextView price=(TextView)row.findViewById(R.id.price_text_quotation);
        TextView status_txt=(TextView)row.findViewById(R.id.status);
        quotation_id.setText(this.quotation_id.get(position));
        quotation.setText(user.get(position));
        date.setText(quo_date.get(position));
        price.setText(prices.get(position));
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
