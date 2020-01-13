package com.health.requestHandler;

import com.health.objects.Strings;

import java.io.IOException;

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
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, MediaType.parse(Strings.APPLICATION_JSON));
        Request req = new Request.Builder().url(Strings.HTTP_LOCALHOST_SYSTEM).post(body).build();
        try {
            Response response = client.newCall(req).execute();
            if (!response.isSuccessful() && num > 0) {
                num--;
                System.out.println("Connection Failed Retrying " + num);
                return send();
            } else {
                num = 5;
            }
            return response.body().string();
        } catch (IOException e) {
            System.out.println("Connection Failed  " + num);
            return "Error 444";
        }

    }

}
