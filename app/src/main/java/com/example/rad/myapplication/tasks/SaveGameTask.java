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
    private String message;;
    private Integer id=null;
    private String code=null;

    public Integer getGameId(){

        return this.id;
    }

    public String getGameCode(){

        return this.code;
    }


    protected SaveGameTask( SaveGame data) {
        this.data = data;
    }
    public String getMessage() {

        return message;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            LOG.debug("Save Game: "+ new Gson().toJson(data));
            String result =  client.postURLWithAuth(Constants.SAVE_ON_GAME_URL, String.class,new Gson().toJson(data));
            LOG.error(result);
            JSONObject jsonObject = new JSONObject(result);

            if(jsonObject.has("code") && jsonObject.has("id")) {
                id = jsonObject.getInt("id");
                code = jsonObject.getString("code");
                return true;
            }
            if(jsonObject.has("error") && !jsonObject.getString("error").equals("")){
                message =jsonObject.getString("error");
                LOG.error(message);
            }
            return false;

        } catch (ApiException | JSONException exp) {
            LOG.error(exp.getMessage(), exp);
        }
        return false;
    }
}
