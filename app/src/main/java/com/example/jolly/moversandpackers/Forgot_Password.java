package com.example.jolly.moversandpackers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Forgot_Password extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter_Security_Question_Forgot_Password;
    ArrayList<String> arrayList_Security_Question_Forgot_Password;
    Spinner spinner_question;
    Button check;
    EditText Name,Answer,New_pas,Confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        setTitle("Forgot Password");
        spinner_question=(Spinner)findViewById(R.id.Sequrity_Question_Forgot_Password);
        Name=(EditText)findViewById(R.id.User_Name_Forgot_Password);
        Answer=(EditText)findViewById(R.id.Answer_Edit_Forgot_Password);
        New_pas=(EditText)findViewById(R.id.New_Password);
        Confirm_password=(EditText)findViewById(R.id.Confirm_Edit_Forgot_password);
        check=(Button)findViewById(R.id.Submit_Button);
        arrayList_Security_Question_Forgot_Password=new ArrayList<>();
        arrayList_Security_Question_Forgot_Password.addAll(Arrays.asList(getResources().getStringArray(R.array.Security_Question)));
        arrayAdapter_Security_Question_Forgot_Password=new ArrayAdapter<String>(Forgot_Password.this,android.R.layout.simple_list_item_1,arrayList_Security_Question_Forgot_Password);
        spinner_question.setAdapter(arrayAdapter_Security_Question_Forgot_Password);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String name_text=Name.getText().toString().trim();
                String answer=Answer.getText().toString().trim();
                String pass=New_pas.getText().toString().trim();
                String cpass=Confirm_password.getText().toString().trim();


                if(name_text.isEmpty() && name_text.length()<=0){
                    Name.setError("Enter Name");
                }
                if(answer.isEmpty() && answer.length()<=0){
                    Answer.setError("Enter Address");
                }
                if(pass.isEmpty() && pass.length()<=0){
                    New_pas.setError("Enter New Password");
                }
                if(cpass.isEmpty() && cpass.length()<=0){
                    Confirm_password.setError("Enter Confirm Password");
                }
                if(!New_pas.getText().toString().equals(Confirm_password.getText().toString())){
                    Confirm_password.setError("password Not Match");
                    Confirm_password.requestFocus();
                }

                else{
                    Toast.makeText(Forgot_Password.this,"Ok",Toast.LENGTH_SHORT).show();
                }*/

                emptyLogin();
                checkPassword();
            }
        });


    }
    public void emptyLogin(){
        String name_text=Name.getText().toString().trim();
        String answer=Answer.getText().toString().trim();
        String pass=New_pas.getText().toString().trim();
        String cpass=Confirm_password.getText().toString().trim();

        if(name_text.isEmpty() && name_text.length()<=0){
            Name.setError("Enter Name");
        }
        if(answer.isEmpty() && answer.length()<=0){
            Answer.setError("Enter Address");
        }
        if(pass.isEmpty() && pass.length()<=0){
            New_pas.setError("Enter New Password");
        }
        if(cpass.isEmpty() && cpass.length()<=0){
            Confirm_password.setError("Enter Confirm Password");
        }
    }
    public void checkPassword(){
        String pass=New_pas.getText().toString().trim();
        String cpass=Confirm_password.getText().toString().trim();

        if(!New_pas.getText().toString().equals(Confirm_password.getText().toString())){
            Confirm_password.setError("password Not Match");
            Confirm_password.requestFocus();
        }

        /*else{
            Toast.makeText(Forgot_Password.this,"Ok",Toast.LENGTH_SHORT).show();
        }*/
    }
}
