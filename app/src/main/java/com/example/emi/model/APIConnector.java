package com.example.emi.model;

import android.content.Context;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.StringEntity;

public class APIConnector {
    private static final String BASE_URL = "https://steffen.cloud/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }


    //JSONObject welches eingefügt werden soll, entspricht der StringEntity
    //Das Object wird in der RestUtils Klasse übergeben und umgewandelt
    //Context kann immer null sein, und ContentType wird automatisch gesetzt
    public static void post(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-Type", "application/json");
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static String getAbsoluteUrl(String relativeUrl, int id) {
        return BASE_URL + relativeUrl + "/" + id;
    }

}
