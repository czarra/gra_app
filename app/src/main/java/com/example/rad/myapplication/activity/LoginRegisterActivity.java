package com.example.rad.myapplication.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


import android.widget.EditText;
import android.widget.Toast;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.LoginUser;
import com.example.rad.myapplication.tasks.LoginUserTask;

public class LoginRegisterActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText emailField, passwordField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        sharedPreferences =
                getApplicationContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);

        passwordField = (EditText) findViewById(R.id.password);
        emailField = (EditText) findViewById(R.id.email);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:

                    LoginUser credentials = LoginUser.regular(emailField.getText().toString(), passwordField.getText().toString());
                        LoginUserTask login = new LoginUserTask(sharedPreferences, credentials) {
                            @Override
                            protected void onPostExecute(Boolean success) {
                                super.onPostExecute(success);
                                if (success) {
                                    Toast.makeText(getApplicationContext(),
                                            "Udało się...",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Nie udało się zalogować22...",Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        login.execute();
                break;
          default:
              break;
        }
    }
}
