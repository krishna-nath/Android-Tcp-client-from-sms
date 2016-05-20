package com.example.krishnanathv.complete_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Krishna Nath V on 25-Mar-16.
 */
public class myclass extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }




    // Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), MainActivity.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MainActivity.class));
    }
}
