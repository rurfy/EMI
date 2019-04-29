package com.example.emi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Interface dient zur besseren Bedienung der REST-GET-Requests
//Durch Implementierung in eine Klasse kann ein JSONObject oder JSONArray erhalten werden
public interface OnJSONResponseCallback {

    void onJSONResponse(JSONArray response);
    void onJSONResponse(JSONObject response);
}
