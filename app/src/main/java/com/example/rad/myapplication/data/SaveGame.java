package com.example.rad.myapplication.data;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SaveGame {

    private Integer gameId;

    private String code;

    private String name;

    private String description;


    public SaveGame(String code) {
        this.code = code;
        this.gameId = 0;
        this.name="";
        this.description="";
    }

    public SaveGame(Integer gameId,String code,  String name,  String description ) {
        this.code = code;
        this.gameId = gameId;
        this.name = name;
        this.description = description;
    }

    public static SaveGame fromJsonObject(JSONObject jsonObject) {
        try {
            return new SaveGame(jsonObject.getInt("id"),
                    jsonObject.getString("code"),
                    jsonObject.getString("name"),
                    jsonObject.getString("description"));
        } catch (JSONException exp) {
            Log.e("SaveGame class", exp.getMessage());
        }
        return null;
    }

    public String getCode() {

        return code;
    }

    public Integer getGameId() {

        return gameId;
    }
}
