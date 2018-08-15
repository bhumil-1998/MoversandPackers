package com.example.jolly.moversandpackers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Customer_profile extends AppCompatActivity {
    Button chk,submit;
    EditText name,address,phoneno,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile");
        setContentView(R.layout.activity_customer_profile);
        Email=(EditText)findViewById(R.id.email_profile);
        name=(EditText)findViewById(R.id.name_profile);
        address=(EditText)findViewById(R.id.address_profile);
        phoneno=(EditText)findViewById(R.id.phone_profile);
        submit=(Button)findViewById(R.id.submit_profile);
        /*chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpassword.setVisibility(View.VISIBLE);
                newpassword.setVisibility(View.VISIBLE);

            }
        });*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstr=Email.getText().toString().trim();
                String namestr=name.getText().toString().trim();
                String addstr=address.getText().toString().trim();
                String phonenostr=phoneno.getText().toString().trim();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(emailstr.matches(emailPattern)){
                    Toast.makeText(Customer_profile.this,"Valid",Toast.LENGTH_SHORT).show();
                   }
                else{
                    Email.setError("Invalid");
                    }
                if(namestr.isEmpty() && namestr.length()<=0){
                    name.setError("Enter Name");
                }
                else if(addstr.isEmpty() && addstr.length()<=0){
                    address.setError("Enter Address");
                }
                else if(phonenostr.isEmpty() && phonenostr.length() <=10){
                    phoneno.setError("Enter Phone No");
                }
                else{
                    Toast.makeText(Customer_profile.this,"ok",Toast.LENGTH_LONG).show();
                }


            }
        });



    }
}
