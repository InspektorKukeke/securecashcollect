package com.example.ssalu.cashcollect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Splash screen code
//Thread is put to sleep for 3 seconds, after which user is directed to main activity and splash is activity finished
public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
