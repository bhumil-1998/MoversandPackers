package com.example.jolly.moversandpackers;

import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try{
                    sleep(2500);
                    Intent intent=new Intent(Splash.this,Visitor_main.class);
                    startActivity(intent);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();
    }
}

