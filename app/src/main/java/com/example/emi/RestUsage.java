package com.example.emi;

import android.util.Log;

import com.loopj.android.http.*;

import org.json.*;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RestUsage {

    public static JSONArray getAllItems(String type) {
        final JSONArray[] allItems = {null};
        APIConnector.get(type, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                allItems[0] = response;
            }

        });
        return allItems[0];
    }

    public static JSONObject getOneItem(String type, int id) {
        final JSONObject[] item = {null};
        APIConnector.get(type + "/" + id, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject repsonse) {
                item[0] = repsonse;
            }

        });
        return item[0];

    }

    public static void postOneItem() {

        APIConnector.post("Ticket", null, new JsonHttpResponseHandler());
        Log.e("Test123", "fertig");
    }
}
