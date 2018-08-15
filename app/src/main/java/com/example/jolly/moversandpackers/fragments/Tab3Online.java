package com.example.jolly.moversandpackers.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Created by intel on 05/02/2018.
 */

public class Tab3Online extends Fragment {
    ListView listView;
    List nameList = new ArrayList();
    List qtyList = new ArrayList();
    int namesize, qtysize;
    Button button;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        button = (Button) rootView.findViewById(R.id.btn3);
        listView = (ListView) rootView.findViewById(R.id.list_tab3);
        new asc().execute();
        List modelList = new ArrayList<>();
        String[] Kitchen = {"gas stove", "water filter", "Aqua Guard", "Grinder", "Kitchen Rack"};
        for (int i = 0; i < Kitchen.length; i++) {
            textModel model = new textModel();
            model.setName(Kitchen[i]);
            modelList.add(model);
        }
        final textAdapter adapter = new textAdapter(modelList, getActivity());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList = adapter.getNameList();
                qtyList = adapter.getQtyList();
                namesize = nameList.size();
                for (int i = 0; i < namesize; i++) {
                    Log.i("Member name: ", nameList.get(i).toString() + qtyList.get(i).toString());
                }
                new asc().execute();
            }
        });
        listView.setAdapter(adapter);
        return rootView;
    }

    public class asc extends AsyncTask<String, String, String> {
        String result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("request_id",item.request_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray();
            try {
                for (int i = 0; i < namesize; i++) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("name", nameList.get(i));
                    jsonArray.put(jsonObject1);
                    jsonObject1.put("qty", qtyList.get(i));
                }
                jsonObject.put("Name", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            result = WebInterface.doWS("https://packersandmovers-virali.000webhostapp.com/moversandpackers/itemInsertJson.php", jsonObject.toString());
            return null;
        }
    }
}

