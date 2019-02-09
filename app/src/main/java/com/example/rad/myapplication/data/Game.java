package com.example.rad.myapplication.data;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Game {

    private Integer gameId;

    private String code;

    private String name;

    private String description;

    private Boolean isCurrentTask;

    private Integer allTask;

    private Integer userTask;

    private String imageUrl  = "";

    private String time = "";



    public Game(String code) {
        this.code = code;
        this.gameId = 0;
        this.name="";
        this.description="";
        this.isCurrentTask = null;
        this.allTask = null;
        this.userTask = null;
    }

    public Game( ) {
        this.code = null;
        this.gameId = null;
        this.name = null;
        this.description = null;
        this.isCurrentTask = null;
        this.allTask = null;
        this.userTask = null;
    }
    private Game(Integer gameId, String code, String name, String description ) {
        this.code = code;
        this.gameId = gameId;
        this.name = name;
        this.description = description;
        this.isCurrentTask = null;
        this.allTask = null;
        this.userTask = null;
    }

    private Game(Integer gameId, String code, String name,
                    String description, boolean isCurrentTask, Integer allTask, Integer userTask,
                    String imageUrl, String time) {
        this.code = code;
        this.gameId = gameId;
        this.name = name;
        this.description = description;
        this.isCurrentTask = isCurrentTask;
        this.allTask = allTask;
        this.userTask = userTask;
        this.imageUrl = imageUrl;
        this.time = time;
    }

    public static Game fromJsonObject(JSONObject jsonObject, Boolean bigger) {
        try {
            if(!bigger) {
                return new Game(jsonObject.getInt("id"),
                        jsonObject.getString("code"),
                        jsonObject.getString("name"),
                        jsonObject.getString("description"));
            }else {

                boolean locIsCurrentTask = jsonObject.getInt("isCurrentTask") > 0 ? true : false ;
                return new Game(jsonObject.getInt("id"),
                        jsonObject.getString("code"),
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        locIsCurrentTask,
                        jsonObject.getInt("allTask"),
                        jsonObject.getInt("userTask"),
                        jsonObject.getString("img_url"),
                        jsonObject.getString("time"));
            }
        } catch (JSONException exp) {
            Log.e("Game class", exp.getMessage());
        }
        return null;
    }

    public String getCode() {

        return code;
    }

    public Integer getGameId() {

        return gameId;
    }

    public String getName() {

        return name;
    }

    public String getDescription() {

        return description;
    }


    public Boolean getIsCurrentTask() {

        return isCurrentTask;
    }

    public Integer getAllTask() {

        return allTask;
    }

    public Integer getUserTask() {

        return userTask;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getTime(){
        return time;
    }
}
