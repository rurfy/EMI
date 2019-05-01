package com.example.emi.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.*;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestUtils {

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


    //Das Objekt jsonObjekt wird in die Tabelle table per POST-Methode hinzugefügt
    public static void postOneItem(JSONObject jsonObject, String table, final Context context) {

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonObject.toString());
            entity.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        APIConnector.post(null, table, entity, "application/json", new AsyncHttpResponseHandler() {

            //@Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, "Das Ticket wurde erfolgreich angelegt.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseString, Throwable throwable) {

            }
        });
    }
}
