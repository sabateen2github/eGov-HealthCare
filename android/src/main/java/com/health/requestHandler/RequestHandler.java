package com.health.requestHandler;

import android.util.Log;

import com.health.objects.Strings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestHandler {
    private String json;

    public RequestHandler(String json) {

        this.json = json;

    }

    private static int num = 5;

    //return json response
    public String send() {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.MINUTES).readTimeout(100, TimeUnit.MINUTES)
                .writeTimeout(100, TimeUnit.MINUTES)
                .build();

        RequestBody body = null;
        try {
            body = RequestBody.create(json.getBytes("UTF-8"), MediaType.parse(Strings.APPLICATION_JSON));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request req = new Request.Builder().url(Strings.HTTP_LOCALHOST_SYSTEM).post(body).build();
        try {
            Response response = client.newCall(req).execute();
            if (!response.isSuccessful() && num > 0) {
                num--;
                return send();
            } else {
                num = 5;
            }

            String data = new String(response.body().bytes(), StandardCharsets.UTF_8);
            return data;
        } catch (IOException e) {

            return Strings.ERROR_444;
        }

    }

}
