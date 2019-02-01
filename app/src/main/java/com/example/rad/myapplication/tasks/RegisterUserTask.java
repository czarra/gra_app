package com.example.rad.myapplication.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.LoginUser;
import com.example.rad.myapplication.data.RegisterUser;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterUserTask extends AsyncTask<String, String, Boolean> {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUserTask.class);

    private final RegisterUser credentials;
    private final ApiClient client = ApiClient.getInstance();

    protected RegisterUserTask( RegisterUser credentials) {
        this.credentials = credentials;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            LOG.error(Constants.Register_URL);
            //LOG.error(credentials.getPassword());
            String result = client.postURL(Constants.Register_URL, String.class, new Gson().toJson(credentials));
            LOG.error(result);
            JSONObject jsonObject = new JSONObject(result);
            //LOG.error( jsonObject.toString());
            if (jsonObject.has("ok") && jsonObject.getString("ok")=="true") {
                LOG.error( jsonObject.getString("ok"));
                return true;
            }

        } catch (Exception exp) {
            LOG.error(exp.getMessage(), exp);
        }
        return false;
    }
}
