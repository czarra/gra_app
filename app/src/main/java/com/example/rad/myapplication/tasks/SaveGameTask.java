package com.example.rad.myapplication.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.api.ApiException;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.LoginUser;
import com.example.rad.myapplication.data.SaveGame;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveGameTask extends AsyncTask<String, String, Boolean> {

    private static final Logger LOG = LoggerFactory.getLogger(SaveGameTask.class);

    private SaveGame data;
    private final ApiClient client = ApiClient.getInstance();
    private String message;

    protected SaveGameTask(SaveGame data) {

        this.data = data;
    }
    public String getMessage() {

        return message;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            LOG.debug("Save Game: "+ new Gson().toJson(data));
            String result =  client.getURLWithAuth(Constants.NUMBER_TEST_URL+"?code="+data.getCode(), String.class);
            LOG.error(result);
            JSONObject jsonObject = new JSONObject(result);

            if(jsonObject.has("http_code") && !jsonObject.getString("http_code").equals("200")){
                return false;
            }
            return true;
        } catch (ApiException | JSONException exp) {
            LOG.error(exp.getMessage(), exp);
        }
        return false;
    }
}
