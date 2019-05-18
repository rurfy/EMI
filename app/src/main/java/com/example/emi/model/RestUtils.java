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
    public static void getAllItems(String table, final Context context, final OnJSONResponseCallback callback) {

        APIConnector.get(table, null, new JsonHttpResponseHandler() {


            //Es können insgesamt 4 Methoden genutzt werden
            //Neben onSuccess() sind das onStart(), onFailure() und onRetry()
            //onStart() würde beim Start des Abrufs ausgeführt werden
            //onSucces() wird bei einer funktionierenden Verbindung ausgeführt
            //onFailure wird ausgeführt, wenn die Verbindung fehlschlägt
            //onRetry() wird ausgeführt, wenn der Verbindungsversuch wiederholt wird

            @Override
            public void onStart() {
                super.onStart();
                Log.d(context.getClass().getSimpleName(), "API Request via GET-Methode wird ausgeführt.");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                //Dieser Befehl repräsentiert den Aufruf in der Aufruferklasse
                //Das erhaltene JSONAraay wird in jedem Fall hierüber in die Aufruferklasse übergeben
                Log.d(context.getClass().getSimpleName(), "Übertragung erfolgreich. Response: " + response.toString());
                callback.onJSONResponse(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(context.getClass().getSimpleName(), "Übertragung fehlgeschlagen. Fehlercode: " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                Log.e(context.getClass().getSimpleName(), "Retry der Verbindung. Versuch Nummer: " + retryNo);
            }
        });
    }


    //Das Objekt jsonObjekt wird in die Tabelle table per POST-Methode hinzugefügt
    public static void postOneItem(JSONObject jsonObject, String table, final Context context, final OnJSONResponseCallback callback) {

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonObject.toString());
            entity.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        APIConnector.post(null, table, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                Log.d(context.getClass().getSimpleName(), "API Request via POST-Methode wird ausgeführt.");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, "Das Ticket wurde erfolgreich angelegt.", Toast.LENGTH_LONG).show();
                Log.d(context.getClass().getSimpleName(), "Datensatz angelegt unter folgender ID: " + new String(responseBody));

                JSONArray response;
                try {
                    response = new JSONArray(new String(responseBody));
                    callback.onJSONResponse(response.getJSONObject(0).getString("TicketID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseString, Throwable throwable) {

                Log.e(context.getClass().getSimpleName(), "Daten konnten nicht gesendet werden. Fehlercode: " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                Log.e(context.getClass().getSimpleName(), "Retry der Verbindung. Versuch Nummer: " + retryNo);
            }
        });
    }


    //Das Objekt jsonObjekt wird in die Tabelle table per POST-Methode hinzugefügt
    public static void updateOneItem(JSONObject jsonObject, String table, final Context context, final OnJSONResponseCallback callback) {


            StringEntity entity = null;
            try {
                entity = new StringEntity(jsonObject.toString());
                entity.setContentType("application/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            APIConnector.post(null, table, entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                    Log.d(context.getClass().getSimpleName(), "API Request via POST-Methode wird ausgeführt.");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(context, "Das Ticket wurde erfolgreich angelegt.", Toast.LENGTH_LONG).show();
                    Log.d(context.getClass().getSimpleName(), "Datensatz aktualisiert");

                    JSONArray response;
                    try {
                        response = new JSONArray(new String(responseBody));
                        callback.onJSONResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseString, Throwable throwable) {

                    Log.e(context.getClass().getSimpleName(), "Daten konnten nicht gesendet werden. Fehlercode: " + statusCode);
                }

                @Override
                public void onRetry(int retryNo) {
                    super.onRetry(retryNo);
                    Log.e(context.getClass().getSimpleName(), "Retry der Verbindung. Versuch Nummer: " + retryNo);
                }
            });
        }
}
