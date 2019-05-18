package com.example.emi.model;

import org.json.JSONArray;

//Interface dient zur besseren Bedienung der REST-GET-Requests
//Durch Implementierung in eine Klasse kann ein JSONObject oder JSONArray erhalten werden
public interface OnJSONResponseCallback {

    void onJSONResponse(JSONArray response);

    void onJSONResponse(String id);
}
