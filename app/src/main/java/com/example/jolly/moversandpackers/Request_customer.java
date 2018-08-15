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

//import static com.example.jolly.moversandpackers.R.id.btn_home_menu;

public class Request_customer extends AppCompatActivity {
    String request_date_str;
    ProgressDialog progressDialog;
    ArrayAdapter<String> request_date_adapter;
    ListView request_date_listview;
    ArrayList<String> request_id;
    ArrayList<String>request_date;
    ArrayList<String> request_source;
    ArrayList<String>request_destination;
    ArrayList<String>request_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_customer);
        setTitle("Requests");
        request_id=new ArrayList<>();
        request_date=new ArrayList<>();
        request_source=new ArrayList<>();
        request_destination=new ArrayList<>();
        request_status=new ArrayList<>();
        new getrequest().execute();
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        /**  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          setSupportActionBar(toolbar);
          getSupportActionBar().setIcon(R.drawable.ic_home);

          toolbar.setNavigationOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(getApplicationContext(),Customer_main.class));
              }
          });*/
        request_date_listview = (ListView) findViewById(R.id.listview_request);
        request_date_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_re;

                switch (position) {

                    default:
                        request_date_str = request_date.get(position);
                        id_re=Integer.parseInt(request_id.get(position));
                        break;
                }
                Intent i=new Intent(Request_customer.this,Request_customer_detail.class);
                i.putExtra("date",request_date.get(position));
                i.putExtra("request_id",id_re);
                i.putExtra("source",request_source.get(position));
                i.putExtra("destination",request_destination.get(position));
                startActivity(i);
            }

        });
    }
  public class getrequest extends AsyncTask<String,String,String>{

      @Override
      protected String doInBackground(String... params) {
          ApiResponse response=new ApiResponse();
          List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
          nameValuePairs.add(new BasicNameValuePair("email_id", GlobalVariable.uname));
          String link="https://packersandmovers-virali.000webhostapp.com/moversandpackers/getRequestDate.php";
          response= WebInterface.doPost(link,nameValuePairs);
          String json= WebInterface.getData(link);
          return response.response;
      }
      @Override
      protected void onPreExecute() {
          super.onPreExecute();
          progressDialog = new ProgressDialog(Request_customer.this);
          progressDialog.setMessage("Please Wait......");
          progressDialog.setCancelable(false);
          progressDialog.show();
      }

      @Override
      protected void onPostExecute(String s) {
          String json=s;
          String[] dateArray=new  String[request_date.size()];
          String[] source=new  String[request_source.size()];
          String[] destination=new  String[request_destination.size()];
          String[] status_arr=new  String[request_status.size()];
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
                          String Request_id = jsonObject1.getString("Request_id");
                          String Request_Date = jsonObject1.getString("Request_Date");
                          String source_place=jsonObject1.getString("Source_Place");
                          String destination_place=jsonObject1.getString("Destination_Place");
                          String status_str=jsonObject1.getString("Status");
                          request_id.add(Request_id);
                          request_date.add(Request_Date);
                          request_source.add(source_place);
                          request_destination.add(destination_place);
                          request_status.add(status_str);
                      }

                      dateArray=request_date.toArray(dateArray);
                      Log.d("array",dateArray.toString());
                      source=request_source.toArray(source);
                      destination=request_destination.toArray(destination);
                      status_arr=request_status.toArray(status_arr);
                  }
                  else {
                      Toast.makeText(Request_customer.this, "", Toast.LENGTH_LONG).show();
                  }
              }
              catch (JSONException e) {
                  e.printStackTrace();
              }
          }
          else{
              Toast.makeText(Request_customer.this, "Cannot get json from server", Toast.LENGTH_SHORT).show();
          }

          //request_date_adapter = new MyAdapter(Request_customer.this,dateArray,source,destination,status_arr);
          request_date_adapter=new MyAdapter(Request_customer.this,request_date,request_source,request_destination,request_status);
          request_date_listview.setAdapter(request_date_adapter);
          super.onPostExecute(s);
      }

  }
 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_home_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
          /*  case btn_home_menu:
                intent = new Intent(this, Customer_main.class);
                startActivity(intent);
                break;*/
            case R.id.btn_feedback_menu:
                intent = new Intent(this, Feedback.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends ArrayAdapter {
        //String[] dateArray,source_arr,destination_arr,status;
        ArrayList<String> date=new ArrayList<>();
        ArrayList<String> source=new ArrayList<>();
        ArrayList<String> destination=new ArrayList<>();
        ArrayList<String> status=new ArrayList<>();


        public MyAdapter(Context context, ArrayList<String> title, ArrayList title2,ArrayList title3,ArrayList title4){
            super(context, R.layout.list_request, R.id.date_text_view,title);
            this.date=title;
            this.source=title2;
            this.destination=title3;
            this.status=title4;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.list_request,parent,false);
            TextView date = (TextView) row.findViewById(R.id.date_text_view);
            TextView source=(TextView)row.findViewById(R.id.source_place_text);
            TextView destination=(TextView)row.findViewById(R.id.destination_place_text);
            TextView status_txt=(TextView)row.findViewById(R.id.status);
            date.setText(this.date.get(position));
            source.setText(this.source.get(position));
            destination.setText(this.destination.get(position));
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