package com.example.rad.myapplication.api;




import com.jcabi.aspects.Cacheable;
import com.jcabi.aspects.Loggable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;



public final class ApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(ApiClient.class);
    private String authToken = null;

    private static class ApiClientHelper {
        private static final ApiClient INSTANCE = new ApiClient();
    }

    public static ApiClient getInstance() {
        return ApiClientHelper.INSTANCE;
    }

    private ApiClient() {
    }
    @Loggable
    public final void authorize(String authToken) {
        this.authToken = authToken;
    }

    @Loggable
    public final void unauthorize() {
        authToken = null;
    }


    private boolean isAuthorized() {
        return authToken != null;
    }

    private void checkIfAuthorized() throws ApiException {
        if (!isAuthorized()) {
            throw new ApiException(401, "Caller unauthorized - token is empty");
        }
    }

    private String readResponse(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        byte[] bytes = new byte[ConnectionBuilder.BUFFER_SIZE];
        StringBuilder sb = new StringBuilder();

        try (InputStream stream = inputStream) {
            int numRead;
            while ((numRead = stream.read(bytes)) >= 0) {
                String line = new String(bytes, 0, numRead);
                sb.append(line);
            }
        }

        return sb.toString();
    }

    private <Response> Response getEntity(String body, Class<Response> responseClass) {

        if (responseClass == Void.class) {
            return null;
        }
        if (responseClass == String.class) {
            return body != null ? (Response) body : (Response) "";
        }

        return emptyEntity(responseClass);
    }

    private <Response> Response emptyEntity(Class<Response> responseClass) {
        try {
            return responseClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Class " + responseClass.getName() + " is required to have accessible no-args constructor", e);
        }
    }

    private <Response> Response createResponse(HttpURLConnection connection, Class<Response> responseClass)
            throws ApiException {
        try {
            int status = connection.getResponseCode();


            if (status == HttpURLConnection.HTTP_OK) {
                String body = readResponse(connection.getInputStream());
                return getEntity(body, responseClass);
            }
            if (status == HttpURLConnection.HTTP_CONFLICT) {

                String body = readResponse(connection.getErrorStream());
                return getEntity(body, responseClass);
            }
            String errorBody = readResponse(connection.getErrorStream());
            throw new ApiException(status, errorBody);
        } catch (IOException e) {
            throw new ApiException(500, "IOException", e);
        }
    }

    public synchronized <Response> Response getURL(String url, Class<Response> responseClass) throws ApiException {
        HttpURLConnection connection = null;
        try {
            connection = new ConnectionBuilder()
                    .withUrl(url)
                    .get();
            return createResponse(connection, responseClass);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Loggable
    @Cacheable(lifetime = 2, unit = TimeUnit.MINUTES)
    public synchronized <Response> Response getURLWithAuth(String url, Class<Response> responseClass)
            throws ApiException {
        checkIfAuthorized();
        HttpURLConnection connection = null;
        try {
            connection = new ConnectionBuilder()
                    .withUrl(url)
                    .withAuthorization(authToken)
                    .get();
//            LOG.error(authToken);
            LOG.error(connection.toString());
            return createResponse(connection, responseClass);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Loggable
    @Cacheable.FlushAfter
    public synchronized <Response> Response postURL(String url, Class<Response> responseClass, String data)
            throws ApiException {
        HttpURLConnection connection = null;
        try {
            connection = new ConnectionBuilder()
                    .withUrl(url)
                    .post(data);
            return createResponse(connection, responseClass);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Loggable
    @Cacheable.FlushAfter
    public synchronized <Response> Response postURLWithAuth(String url, Class<Response> responseClass, String data)
            throws ApiException {
        checkIfAuthorized();
        HttpURLConnection connection = null;
        try {
            connection = new ConnectionBuilder()
                    .withUrl(url)
                    .withAuthorization(authToken)
                    .post(data);
            return createResponse(connection, responseClass);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
