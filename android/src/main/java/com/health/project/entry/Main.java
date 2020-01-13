package com.health.project.entry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.health.fragments.SignInFragment;


public class Main extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new SignInFragment()).commit();
        }

    }


}
