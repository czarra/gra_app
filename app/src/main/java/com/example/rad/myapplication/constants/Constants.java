package com.example.rad.myapplication.constants;


public class Constants {


    public final static String APP_NAME = "Gra";
    private final static String BASE_URL = "http://rportfolio.pl/";
    private final static String BASE_URL_API = BASE_URL+"api/";
    private final static String BASE_URL_LOG_REG = BASE_URL+"login_register_api/";
    public final static String Register_URL = BASE_URL_LOG_REG + "register";
    public final static String Login_URL = BASE_URL_LOG_REG + "login";
    public final static String NUMBER_TEST_URL = BASE_URL_API + "number";
    public final static String SAVE_ON_GAME_URL = BASE_URL_API + "save-on-game";

    public final static String GET_INFO_GAME_URL = BASE_URL_API + "get-info-game";//code
    public final static String GET_CURRENT_TASK_URL = BASE_URL_API + "get-current-task";
    public final static String ALL_USER_GAME_URL = BASE_URL_API + "all-user-games"; //empty
    public final static String ALL_USER_NOT_END_GAME_URL = BASE_URL_API + "all-user-not-end-games"; //empty
    public final static String ALL_USER_END_GAME_URL = BASE_URL_API + "all-user-end-games"; //empty
    public final static String CHECK_TASK_URL = BASE_URL_API + "check-task";


    public final static String SharedPref_Token = "token";
    public final static String SharedPref_ActiveGameId = "active_game_id";

}
