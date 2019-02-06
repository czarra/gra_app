package com.example.rad.myapplication.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.LoginUser;
import com.example.rad.myapplication.data.RegisterUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class RegisterUserTask extends AsyncTask<String, String, Boolean> {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUserTask.class);

    private final RegisterUser credentials;
    private final ApiClient client = ApiClient.getInstance();
    private String message;
    private Map<String,String> errorArray;
    private SharedPreferences sharedPreferences;

    protected RegisterUserTask( SharedPreferences sharedPreferences, RegisterUser credentials) {
        this.sharedPreferences = sharedPreferences;
        this.credentials = credentials;
        this.errorArray= new HashMap<>();
    }

    public Map<String,String> getErrorArray(){
        return this.errorArray;
    }

    public String getMessage() {

        return message;
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
            if (jsonObject.has("ok")
                    && jsonObject.getString("ok").equals("true")
                    && jsonObject.has("apiKey")
                    && !jsonObject.getString("apiKey").isEmpty()) {
                sharedPreferences.edit().putString(Constants.SharedPref_Token, jsonObject.getString("apiKey")).apply();
                return true;
            } else if(jsonObject.has("error")){
                message="";
                JSONObject error = jsonObject.getJSONObject("error");
                Iterator iterator = error.keys();

                while(iterator.hasNext()){
                    String key = (String)iterator.next();
                    String value = error.getString(key);
//                    LOG.error(key);
//                    LOG.error(value);
                    errorArray.put(key,value);
                    message +="   "+key+" : "+value;
                }
            }

        } catch (Exception exp) {
            LOG.error(exp.getMessage(), exp);
        }
        return false;
    }
}
