package com.example.rad.myapplication.api;



import com.google.gson.Gson;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ConnectionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ApiClient.class);
    static final int BUFFER_SIZE = 4096;

    private String method;
    private String url;
    private String contentType = "application/json";
    private String data;
    private String token;
    private final String boundary;

    ConnectionBuilder() {
        this.boundary = "IT" + System.currentTimeMillis();
    }

    ConnectionBuilder withUrl(String url) {
        this.url = url;
        return this;
    }


    HttpURLConnection get() throws ApiException {
        this.method = "GET";
        return build();
    }


    HttpURLConnection post(String data) throws ApiException {
        this.method = "POST";
        this.data = data;
        return build();
    }

    HttpURLConnection post() throws ApiException {
        return post(null);
    }

    ConnectionBuilder withAuthorization(String token) {
        this.token = token;
        return this;
    }

    private HttpURLConnection build() throws ApiException {

        LOG.info("{} {} Content-Type: {}", method, url, contentType);
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept-Language", "pl-PL");

            if (token != null) {
                LOG.info("IS TOKEN ");
                connection.setRequestProperty("Authorization", "Token " + token);
            }

            if (data != null) {
                LOG.info("Request body: {}", data);
                data=convertStringPOST(data);
                byte[] rawData = data.getBytes("UTF-8");
                connection.setRequestProperty("Content-Length", String.valueOf(rawData.length));
                connection.setDoOutput(true);
                connection.getOutputStream().write(rawData);
                connection.getOutputStream().flush();
            }
            connection.connect();
            return connection;
        } catch (IOException e) {
            throw new ApiException(500, "IOException", e);
        }
    }

    private String convertStringPOST(String postData){
        LOG.error("sdfsaff"+postData);
        try{
            JSONObject myjson = new JSONObject(postData);
            Iterator iter = myjson.keys();
            String temp="";
            while(iter.hasNext()){
                String key = (String)iter.next();
                String value = myjson.getString(key);
                temp +=key+"="+value+"&";
            }
            postData=removeLastChar(temp);
        }catch (Exception ex){
            LOG.error(ex.getMessage());
        }
        return postData;
    }
    private static String removeLastChar(String str) {
        if(str.length()>0) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

}