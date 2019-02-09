package com.example.rad.myapplication.data;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

    private Integer taskId;

    private String name;

    private String description;

    private Boolean end = null;

    private Boolean status = null;

    private String imageUrl = "";

    public Task() {
        this.taskId = 0;
        this.name="";
        this.description="";
    }

    private Task(Integer taskId, String name, String description, String imageUrl ) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }


    public static Task fromJsonObject(JSONObject jsonObject) {
        try {
            return new Task(jsonObject.getInt("task_id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("description"),
                    jsonObject.getString("img_url"));

        } catch (JSONException exp) {
            Log.e("Task class", exp.getMessage());
        }
        return null;
    }


    public Integer geTaskId() {

        return taskId;
    }

    public String getName() {

        return name;
    }

    public String getDescription() {

        return description;
    }


    public Boolean getStatus(){
        return this.status;
    }

    public void setStatus(boolean status){
        this.status=status;
    }

    public Boolean getEnd(){
        return this.end;
    }

    public void setEnd(boolean end){
        this.end=end;
    }

    public String getImageUrl(){
        return imageUrl;
    }
}
