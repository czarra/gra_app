package com.example.rad.myapplication.api;



import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rad on 2019-01-28.
 */
class ConnectionBuilder {



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

        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept-Language", "pl-PL");
            connection.connect();
            return connection;
        } catch (IOException e) {
            throw new ApiException(500, "IOException", e);
        }
    }

}