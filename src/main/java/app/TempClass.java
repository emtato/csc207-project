package app;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

// using this to do stuff with the grade api, will delete later. dont run it unless you want to clear the database.
/*
public class TempClass {

    public static void main(String[] args) {
        String DATABASE_USERNAME = "csc207munchablesusername";
        String DATABASE_PASSWORD = "csc207munchablespassword";
        String c = "database";
        String a = "postcommentlikes";
        String b = "usersinformation";
        int CREDENTIAL_ERROR = 401;
        int SUCCESS_CODE = 200;
        String CONTENT_TYPE_LABEL = "Content-Type";
        String CONTENT_TYPE_JSON = "application/json";
        String STATUS_CODE_LABEL = "status_code";
        String USERNAME = "username";
        String PASSWORD = "password";
        String MESSAGE = "message";

        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        // POST METHOD
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, DATABASE_USERNAME);
        requestBody.put(PASSWORD, DATABASE_PASSWORD);
        final JSONObject extra = new JSONObject();
        extra.put(a, new JSONObject());
        extra.put(b, new JSONObject());
        requestBody.put("info", extra);
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/modifyUserInfo")
                .method("PUT", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                System.out.println("success");
            }
            else if (responseBody.getInt(STATUS_CODE_LABEL) == CREDENTIAL_ERROR) {
            }
            else {
            }
        }
        catch (IOException | JSONException ex) {
        }
    }

}

 */
