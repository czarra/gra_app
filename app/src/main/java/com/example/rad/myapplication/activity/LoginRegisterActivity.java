package com.example.rad.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.LoginUser;
import com.example.rad.myapplication.tasks.LoginUserTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginRegisterActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText emailField, passwordField;
    private ProgressBar progressBar ;
    private Button loginButton;
    private static final Logger LOG = LoggerFactory.getLogger(LoginUserTask.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        sharedPreferences =
                getApplicationContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);

        passwordField = (EditText) findViewById(R.id.password);
        emailField = (EditText) findViewById(R.id.email);
        progressBar= (ProgressBar) findViewById(R.id.progressBar1);
        loginButton = (Button)findViewById(R.id.loginButton);

        if (sharedPreferences.contains(Constants.SharedPref_Token)) {
            startMainActivity("Hi");
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                loginButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                    LoginUser credentials = LoginUser.regular(emailField.getText().toString(), passwordField.getText().toString());
                        LoginUserTask login = new LoginUserTask(sharedPreferences, credentials) {
                            @Override
                            protected void onPostExecute(Boolean success) {
                                super.onPostExecute(success);
                                if (success) {
                                    startMainActivity("");
//                                    Toast.makeText(getApplicationContext(),
//                                            "Udało się...",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Nieprawidłowe dane logowania",Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                                loginButton.setEnabled(true);
                            }
                        };
                        login.execute();
                break;
          default:
              break;
        }
    }

    private void startMainActivity(String name) {
        String token = sharedPreferences.getString(Constants.SharedPref_Token, "");
       // LOG.error(token);
        ApiClient.getInstance().authorize(token);
        Intent mIntent = new Intent(LoginRegisterActivity.this, ChoiceSaveActivity.class);
        mIntent.putExtra("name", name);
        startActivity(mIntent);
        finish();
    }
}
