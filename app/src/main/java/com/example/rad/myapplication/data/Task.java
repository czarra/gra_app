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

    public Task() {
        this.taskId = 0;
        this.name="";
        this.description="";
    }

    private Task(Integer taskId, String name, String description ) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
    }


    public static Task fromJsonObject(JSONObject jsonObject) {
        try {
            return new Task(jsonObject.getInt("task_id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("description"));

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
}
