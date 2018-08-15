package com.example.jolly.moversandpackers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jolly.moversandpackers.data.ApiResponse;
import com.example.jolly.moversandpackers.data.WebInterface;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
        ArrayAdapter<String> arrayAdapter_area,arrayAdapter_question;
        ArrayList<String> arrayList_area,arrayList_question;
        Spinner spinner_area,spinner_question;
        EditText mAddress,mPhone_number,mEmail_id,mAnswer,mPassword,user_name,mconfirmpass;

        Button register,reset;


   /* @Override
    protected void onStart() {
        Intent it=getIntent();
        String package_value=it.getStringExtra("value_package");
        Toast.makeText(this, package_value+"", Toast.LENGTH_SHORT).show();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Registration");
        spinner_area=(Spinner)findViewById(R.id.area_spinner_registration);
        spinner_question= (Spinner) findViewById(R.id.Sequrity_Question);
        mAddress= (EditText) findViewById(R.id.address_registration);
        mPhone_number= (EditText) findViewById(R.id.phone_registration);
        mEmail_id= (EditText) findViewById(R.id.email_registration);
        mAnswer= (EditText) findViewById(R.id.answer_editText);
        mPassword= (EditText) findViewById(R.id.password);
        mconfirmpass=(EditText)findViewById(R.id.confirm_password_registration);
        user_name= (EditText) findViewById(R.id.username);
        register= (Button) findViewById(R.id.registration_button);
        reset= (Button) findViewById(R.id.reset_registration);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        arrayList_area=new ArrayList<String>();
        arrayList_area.addAll(Arrays.asList(getResources().getStringArray(R.array.Area)));
        arrayAdapter_area=new ArrayAdapter<String>(Registration.this,android.R.layout.simple_list_item_1,arrayList_area);
        spinner_area.setAdapter(arrayAdapter_area);

        arrayList_question=new ArrayList<String>();
        arrayList_question.addAll(Arrays.asList(getResources().getStringArray(R.array.Security_Question)));
        arrayAdapter_question=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList_question);
        spinner_question.setAdapter(arrayAdapter_question);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=user_name.getText().toString().trim();
                String phone_text=mPhone_number.getText().toString().trim();
                String address_text=mAddress.getText().toString().trim();
                String email_text=mEmail_id.getEditableText().toString().trim();
                String answer_text=mAnswer.getText().toString().trim();
                String password_text=mPassword.getText().toString().trim();
                String question=spinner_question.getSelectedItem().toString();
                String area=spinner_area.getSelectedItem().toString();
                String confirm=mconfirmpass.getText().toString().trim();
                //Toast.makeText(Registration.this, ""+question, Toast.LENGTH_SHORT).show();
                if (phone_text.isEmpty() && phone_text.length()<=0){
                    mPhone_number.setError("enter phone number");
                    //Toast.makeText(Registration.this, "enter name", Toast.LENGTH_SHORT).show();
                }
                if (address_text.isEmpty() && address_text.length()<=0){
                    mAddress.setError("enter address");
                    //Toast.makeText(Registration.this, "enter address", Toast.LENGTH_SHORT).show();
                }

                if(password_text.equals(confirm)){
                    mconfirmpass.setText("valid");
                }
                else {
                    mconfirmpass.setError("Invalid");
                }
                if(email_text.matches(emailPattern)){
                    Toast.makeText(Registration.this,"Valid",Toast.LENGTH_LONG).show();
                    }
                else{
                    Toast.makeText(Registration.this,"Invalid Email",Toast.LENGTH_LONG).show();
                }
                if (answer_text.isEmpty() && answer_text.length()<=0){
                    mAnswer.setError("enter answer");
                    //Toast.makeText(Registration.this, "enter name", Toast.LENGTH_SHORT).show();
                }
                if (password_text.isEmpty() && password_text.length()<=0){
                    mPassword.setError("enter first");
                    //Toast.makeText(Registration.this, "enter name", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(Registration.this,"ok", Toast.LENGTH_SHORT).show();
                    new Async().execute(password_text,address_text,email_text,phone_text,question,answer_text,area,name,confirm);
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            reset();

            }
        });
    }
    public class Async extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Registration.this);
            progressDialog.setMessage("Please Wait......");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String password=strings[0];
            String address=strings[1];
            String email=strings[2];
            String phone=strings[3];
            String question=strings[4];
            String answer=strings[5];
            String area=strings[6];
            String name=strings[7];
            ApiResponse response=new ApiResponse();
            List<BasicNameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Password",password));
            nameValuePairs.add(new BasicNameValuePair("Address",address));
            nameValuePairs.add(new BasicNameValuePair("Email_id",email));
            nameValuePairs.add(new BasicNameValuePair("Mobile_Number",phone));
            nameValuePairs.add(new BasicNameValuePair("Security_Question",question));
            nameValuePairs.add(new BasicNameValuePair("Security_Answer",answer));
            nameValuePairs.add(new BasicNameValuePair("area_name",area));
            nameValuePairs.add(new BasicNameValuePair("User_Name",name));
            String link1="https://packersandmovers-virali.000webhostapp.com/moversandpackers/register.php";
            Log.d("date",nameValuePairs.toString());
            response= WebInterface.doPost(link1,nameValuePairs);
            return response.response;
        }

        @Override
        protected void onPostExecute(String s) {
            String jsonStr=s;
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (jsonStr!=null){
                try {
                    Log.i("json",jsonStr);
                    JSONObject jsonObject=new JSONObject(jsonStr);
                    int query_result = jsonObject.getInt("status");
                    Log.d("query",query_result +"");
                    String message=jsonObject.getString("message");
                    if (query_result==1){
                        Toast.makeText(Registration.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Registration.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Registration.this, ""+message, Toast.LENGTH_SHORT).show();
                        reset();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reset(){
        mAddress.setText("");
        mPhone_number.setText("");
        mAddress.setText("");
        mEmail_id.setText("");
        mAnswer.setText("");
        mPassword.setText("");
    }
}
