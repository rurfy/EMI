package com.example.emi;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.StringEntity;

public class APIConnector {
    private static final String BASE_URL = "https://steffen.cloud/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    //Der Unterschied zur normalen get-Methode besteht darin, dass zusätzlich die ID als int übergeben wird
    //Die zu verwendende URL wird dann automatisch angepasst
    public static void getOne(String url, RequestParams params, int id, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url,id), params, responseHandler);
    }

    public static void post(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static String getAbsoluteUrl(String relativeUrl, int id) {
        return BASE_URL + relativeUrl + "/" + id;
    }

}
