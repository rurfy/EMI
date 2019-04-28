package com.example.emi;

import android.util.Log;

import com.loopj.android.http.*;

import org.json.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestUsage {

    public static JSONArray getAllItems(String type) {
        final JSONArray[] allItems = {null};
        APIConnector.get(type, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                allItems[0] = response;
                try {
                    Log.e("Test123" , response.getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Typ", "INSERT");
            jsonParams.put("Titel", "Test12345");
            jsonParams.put("Datum", "2019-04-28");
            jsonParams.put("Problembeschreibung", "2019 ist toll");
            jsonParams.put("StatusID", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        APIConnector.post(null,"Ticket", entity, "application/json", new JsonHttpResponseHandler());
        Log.e("Test123", "fertig");
    }
}
