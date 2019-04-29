package com.example.emi;

import android.util.Log;

import com.loopj.android.http.*;

import org.json.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestUsage {

    //Das Interface, welches in der Aufruferklasse implmentiert wird, muss hier übergeben werden
    //final ist zwingend notwendig da es von einer inneren Klasse aufgerufen wird
    //String table beschreibt die Tabelle, welche ausgelesen werden soll
    public static void getAllItems(String table, final OnJSONResponseCallback callback) {

        APIConnector.get(table, null, new JsonHttpResponseHandler() {

            //Es können insgesamt 4 Methoden genutzt werden
            //Neben onSuccess() sind das onStart(), onFailure() und onRetry()
            //Gebenenfalls muss das FailureHandling noch implementiert werden

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    //Dieser Befehl repräsentiert den Aufruf in der Aufruferklasse
                    //Das erhaltene JSONAraay wird in jedem Fall hierüber in die Aufruferklasse übergeben
                    callback.onJSONResponse(response);
            }

        });
    }

    public static void getOneItem(String table, int id, final OnJSONResponseCallback callback) {

        APIConnector.getOne(table, null, id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                callback.onJSONResponse(response);
            }
        });

    }

    //Das Objekt jsonObjekt wird in die Tabelle table per POST-Methode hinzugefügt
    public static void postOneItem(JSONObject jsonObject, String table) {

        //################# NUR EIN BEISPIEL ########################################
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
        //################# MUSS VON AUßEN FESTGELEGT WERDEN #########################

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        APIConnector.post(null, table, entity, "application/json", new JsonHttpResponseHandler());
    }
}
