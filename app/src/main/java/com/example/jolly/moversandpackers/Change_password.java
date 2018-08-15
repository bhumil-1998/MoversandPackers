package com.example.jolly.moversandpackers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Change_password extends AppCompatActivity {
    EditText newpassword,confirmpassword1,Emailtext;
    Button changepass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Change Password");
        setContentView(R.layout.activity_change_password);
        newpassword=(EditText)findViewById(R.id.Newpasswordid);
        confirmpassword1=(EditText)findViewById(R.id.confirmpasswordid);
        changepass=(Button)findViewById(R.id.changepasswordid);
        Emailtext=(EditText)findViewById(R.id.Email_change_password);


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chkemail=Emailtext.getEditableText().toString();
                String new_str=newpassword.getText().toString().trim();
                String con_str=confirmpassword1.getText().toString().trim();

                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(chkemail.matches(emailPattern) && chkemail.length()>0){
                    Toast.makeText(Change_password.this,"Valid Email",Toast.LENGTH_SHORT).show();
                }
                else{
                    Emailtext.setError("Invalid Email");
                }
                if(new_str.equals(con_str)){
                    Toast.makeText(Change_password.this,"Your Password is updated",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Change_password.this,"Your Password is not confirmed",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
