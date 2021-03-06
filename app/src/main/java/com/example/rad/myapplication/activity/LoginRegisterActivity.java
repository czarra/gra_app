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
import android.widget.TextView;
import android.widget.Toast;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.LoginUser;
import com.example.rad.myapplication.data.RegisterUser;
import com.example.rad.myapplication.tasks.LoginUserTask;
import com.example.rad.myapplication.tasks.RegisterUserTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.validation.constraints.Null;


public class LoginRegisterActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText emailField, passwordField,usernameRegister,emailRegister,passwordRegister,passwordRegisterRe;
    private TextView usernameRegisterError,emailRegisterError,passwordRegisterError;
    private ProgressBar progressBar ;
    private Button loginButton;
    private Button registerButton;
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
        registerButton = (Button)findViewById(R.id.registerButton);
        usernameRegister = (EditText) findViewById(R.id.usernameRegister);
        emailRegister = (EditText) findViewById(R.id.emailRegister);
        passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        passwordRegisterRe = (EditText) findViewById(R.id.passwordRegisterRe);

        usernameRegisterError = (TextView)findViewById(R.id.usernameRegisterError);
        emailRegisterError = (TextView)findViewById(R.id.emailRegisterError);
        passwordRegisterError = (TextView)findViewById(R.id.passwordRegisterError);
        if (sharedPreferences.contains(Constants.SharedPref_Token)) {
            startMainActivity();
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                if(emailField.getText().toString().equals("")
                        ||  passwordField.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),
                            "Pola nie mogę być puste!", Toast.LENGTH_SHORT).show();

                }else {
                    loginButton.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    LoginUser credentials = LoginUser.regular(emailField.getText().toString(), passwordField.getText().toString());
                    LoginUserTask login = new LoginUserTask(sharedPreferences, credentials) {
                        @Override
                        protected void onPostExecute(Boolean success) {
                            super.onPostExecute(success);
                            if (success) {
                                startMainActivity();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Nieprawidłowe dane logowania", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                            loginButton.setEnabled(true);
                        }
                    };
                    login.execute();
                }
                break;

            case R.id.registerButton:
                usernameRegisterError.setVisibility(View.GONE);
                emailRegisterError.setVisibility(View.GONE);
                passwordRegisterError.setVisibility(View.GONE);
                String rePassword = new String(passwordRegisterRe.getText().toString());
                if(usernameRegister.getText().toString().equals("")
                        ||  emailRegister.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),
                            "Pola nie mogę być puste!", Toast.LENGTH_SHORT).show();

                }else if(!rePassword.isEmpty()
                        && rePassword.equals(passwordRegister.getText().toString())) {

                    registerButton.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    RegisterUser credentialsRegister = RegisterUser.regular(usernameRegister.getText().toString()
                            , emailRegister.getText().toString()
                            , passwordRegister.getText().toString());
                    RegisterUserTask register = new RegisterUserTask(sharedPreferences,credentialsRegister) {
                        @Override
                        protected void onPostExecute(Boolean success) {
                            super.onPostExecute(success);
                            if (success) {
                                startMainActivity();
//                                Toast.makeText(getApplicationContext(),
//                                        "Udało się można się zalogować", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String,String> errors = super.getErrorArray();
                                if(errors.get("email") != null){
                                    emailRegisterError.setText(errors.get("email"));
                                    emailRegisterError.setVisibility(View.VISIBLE);
                                }
                                if(errors.get("username")!= null){
                                    usernameRegisterError.setText(errors.get("username"));
                                    usernameRegisterError.setVisibility(View.VISIBLE);
                                }
                                if(errors.get("password")!= null){
                                    passwordRegisterError.setText(errors.get("password"));
                                    passwordRegisterError.setVisibility(View.VISIBLE);
                                }


                                Toast.makeText(getApplicationContext(),
                                        "Nieprawidłowe dane! ", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                            registerButton.setEnabled(true);
                        }
                    };
                    register.execute();
                } else {
                    passwordRegisterError.setText("Nieprawidłowe hasło");
                    passwordRegisterError.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(),
//                            "Nieprawigłowe hasło!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void startMainActivity() {
        String token = sharedPreferences.getString(Constants.SharedPref_Token, "");
        ApiClient.getInstance().authorize(token);
        Intent mIntent = new Intent(LoginRegisterActivity.this, ChoiceSaveActivity.class);
        startActivity(mIntent);
        finish();
    }
}
