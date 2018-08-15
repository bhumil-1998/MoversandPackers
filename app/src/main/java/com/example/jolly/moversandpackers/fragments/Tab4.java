package com.example.jolly.moversandpackers.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.jolly.moversandpackers.R;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tab4 extends Fragment {
    ListView listView;
    List nameList=new ArrayList();
    List qtyList=new ArrayList();
    int namesize,qtysize;
    Button button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab4, container, false);
        button= (Button) rootView.findViewById(R.id.btn4);
        listView= (ListView) rootView.findViewById(R.id.list_tab4contacts);
        List modelList=new ArrayList<>();
        String[] arrays={"Bags","Acquarium","Books","Wooden Box"};
        for (int i=0 ; i<arrays.length;i++){
            textModel model=new textModel();
            model.setName(arrays[i]);
            modelList.add(model);
        }

        final textAdapter adapter=new textAdapter(modelList,getActivity());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList=adapter.getNameList();
                qtyList=adapter.getQtyList();
                namesize=nameList.size();
                for (int i=0;i<namesize;i++){
                    Log.i("Member name: ", nameList.get(i).toString() + qtyList.get(i).toString());
                }
                new async().execute();
            }
        });
        listView.setAdapter(adapter);
        return rootView;
    }

    public class async extends AsyncTask<String,String,String>{
        String result;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("request_id",item.request_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray=new JSONArray();
            try {
                for(int i=0;i<namesize;i++){
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("name",nameList.get(i));
                    jsonArray.put(jsonObject1);
                    jsonObject1.put("qty",qtyList.get(i));
                }
                jsonObject.put("Name",jsonArray);
                Log.i("name",jsonObject.toString());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            result= WebInterface.doWS("https://packersandmovers-virali.000webhostapp.com/moversandpackers/itemInsertJson.php",jsonObject.toString());
            return null;
        }
    }
}
