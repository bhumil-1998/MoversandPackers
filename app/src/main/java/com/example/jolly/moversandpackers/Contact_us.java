package com.example.jolly.moversandpackers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact_us extends AppCompatActivity {

    private TextView phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setTitle("Contact_us");
       phone = (TextView) findViewById(R.id.buttonCall);
        phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9898376820"));
                if (ActivityCompat.checkSelfPermission(Contact_us.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                  Toast.makeText(Contact_us.this,"grant the permission",Toast.LENGTH_SHORT).show();
                     requestPermission();
                } else {
                    startActivity(callIntent);
                }
            }
            private void requestPermission(){
                ActivityCompat.requestPermissions(Contact_us.this,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        });
    }
}
