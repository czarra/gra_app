package com.example.rad.myapplication.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.data.LoginUser;
import com.example.rad.myapplication.constants.Constants;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUserTask extends AsyncTask<String, String, Boolean> {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserTask.class);

    private SharedPreferences sharedPreferences;
    private final LoginUser credentials;
    private final ApiClient client = ApiClient.getInstance();

    protected LoginUserTask(SharedPreferences sharedPreferences, LoginUser credentials) {
        this.sharedPreferences = sharedPreferences;
        this.credentials = credentials;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            LOG.error(Constants.Login_URL);
            //LOG.error(credentials.getPassword());
            String result = client.postURL(Constants.Login_URL, String.class, new Gson().toJson(credentials));
           // LOG.error(result);
            JSONObject jsonObject = new JSONObject(result);
            //LOG.error( jsonObject.toString());
            if (jsonObject.has("apiKey") && !jsonObject.getString("apiKey").isEmpty()) {
                LOG.error( jsonObject.getString("apiKey"));
                sharedPreferences.edit().putString(Constants.SharedPref_Token, jsonObject.getString("apiKey")).apply();
                return true;
            }

        } catch (Exception exp) {
            LOG.error(exp.getMessage(), exp);
        }
        return false;
    }
}
