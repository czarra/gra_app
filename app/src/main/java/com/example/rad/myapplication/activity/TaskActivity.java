package com.example.rad.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.api.ApiException;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.Game;
import com.example.rad.myapplication.tasks.LoginUserTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskActivity extends AppCompatActivity  {

    private SharedPreferences sharedPreferences;
    private Button checkButton;
    private TextView textName, textDescription;
    private ProgressBar progressBar;
    private String code;

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserTask.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        sharedPreferences =
                getApplicationContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        checkButton = (Button) findViewById(R.id.checkButton);
        textName = (TextView) findViewById(R.id.textName);
        textDescription = (TextView) findViewById(R.id.textDescription);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        code =  intent.getStringExtra("code");


    }

    private void logout() {
        sharedPreferences.edit().clear().apply();
        ApiClient.getInstance().unauthorize();
        finishAffinity();
        Intent mIntent = new Intent(this, LoginRegisterActivity.class);
        startActivity(mIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.logout) {
            logout();

            return true;
        }
        if (id == R.id.gamesAll) {
            startMainActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startMainActivity() {
        Intent mIntent = new Intent(TaskActivity.this, ChoiceSaveActivity.class);
        startActivity(mIntent);
        finish();
    }


//    @Override
//    public void onBackPressed() {
//
//        startMainActivity();
//    }
    
}
