package com.example.rad.myapplication.data;


public class SaveGame {

    private Integer gameId;

    private String code;


    public SaveGame(String code) {
        this.code = code;
        this.gameId = 0;
    }

    public String getCode() {

        return code;
    }

    public Integer getGameId() {

        return gameId;
    }
}
