package com.example.jolly.moversandpackers.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolly.moversandpackers.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 31/01/2018.
 */

public class textAdapter extends BaseAdapter {
    String[] name;
    List<textModel> textModelList;
    Context mContext;
    LayoutInflater inflater;
    String[] qty={"0","1","2","3","4","5","6","7","8","9","10","10+"};
    boolean mSpinnerInitialized=false;
    List nameList=new ArrayList();
    List qtyList=new ArrayList();

    public textAdapter(List<textModel> textModelList, Context mContext) {
        this.textModelList = textModelList;
        this.mContext = mContext;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder{
        TextView textView;
        Spinner spinner;
    }

    @Override
    public int getCount() {
        return textModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return textModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        if (convertView==null){
            convertView=inflater.inflate(R.layout.list_item,null);
            holder=new ViewHolder();
            holder.spinner = (Spinner) convertView.findViewById(R.id.item_spinner);
            ArrayAdapter<String> spinner_adapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,qty);
            holder.spinner.setAdapter(spinner_adapter);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        final ViewHolder finalHolder = holder;
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    /*textModel model=textModelList.get(position);
                    textModelList.remove(position);
                    textModelList.add(position,model);*/
                int pos=0;
                if (!mSpinnerInitialized) {
                    mSpinnerInitialized = true;
                    return;
                }

                //String asdf = finalHolder.spinner.getSelectedItem().toString();
                long p=finalHolder.spinner.getItemIdAtPosition(i);
                String name=finalHolder.textView.getText().toString();
                if (p>0) {
                    if (nameList.contains(name)){
                        int q=nameList.indexOf(name);
                        qtyList.set(q,p);
                    }
                    else {
                        Toast.makeText(mContext, "" + name + p, Toast.LENGTH_SHORT).show();
                        nameList.add(name);
                        qtyList.add(p);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        holder.textView= (TextView) convertView.findViewById(R.id.item_text_view);
        holder.textView.setText(textModelList.get(position).getName());
        return convertView;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    public List getNameList(){
        return nameList;
    }
    public List getQtyList(){
        return qtyList;
    }
}
