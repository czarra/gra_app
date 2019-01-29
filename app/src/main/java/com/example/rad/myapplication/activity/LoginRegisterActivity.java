package com.example.rad.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


import android.widget.Toast;

import com.example.rad.myapplication.R;

public class LoginRegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                Toast.makeText(getApplicationContext(),
                            "Login...",Toast.LENGTH_SHORT).show();
                break;
          default:
              break;
        }
    }
}
