package com.example.emi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//Diese Klasse enthält universale Konvertierungswerkzeuge
//Hier werden JSONArrays in ArrayLists umgewandelt, etc.
public class Utils {

    //Eine gesamte ArrayList vom Typ !!!Ticket!!! aus einem JSONArray erstellen
    public static ArrayList<HashMap<String, String>> jsonToArrayList(JSONArray jsonArray) {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                arrayList.add(jsonObjectToHashMap(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;

    }

    //Eine einzige Hashmap vom Typ !!!Ticket!!! aus einem JSONObject erstellen
    public static HashMap<String, String> jsonObjectToHashMap(JSONObject jsonObject) {
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            hashMap.put("ID", jsonObject.getString("ID"));
            hashMap.put("Titel", jsonObject.getString("Titel"));
            hashMap.put("Datum", jsonObject.getString("Datum"));
            hashMap.put("Problembeschreibung", jsonObject.getString("Problembeschreibung"));
            hashMap.put("StatusID", jsonObject.getString("StatusID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public static String[][] jsonArraytoStingArray(JSONArray jsonArray, String attribute) {
        int size = jsonArray.length();
        String[][] stringArray = new String[size][2];

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                stringArray[i][0] = (jsonArray.getJSONObject(i)).getString("ID");
                stringArray[i][1] = (jsonArray.getJSONObject(i)).getString(attribute);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stringArray;
    }
}
