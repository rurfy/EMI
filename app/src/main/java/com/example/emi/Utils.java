package com.example.emi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

        Iterator jsonIterator = jsonObject.keys();
        while(jsonIterator.hasNext()) {
            String key = (String) jsonIterator.next();
            String value = null;
            try {
                value = (String) jsonObject.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hashMap.put(key, value);
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

    public static JSONObject prepareDataForPost(HashMap<String,String> hashMap) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Typ", "INSERT");
            Iterator mapIterator = hashMap.keySet().iterator();
            while(mapIterator.hasNext()) {
                String key = (String)mapIterator.next();
                String value = hashMap.get(key);
                jsonParams.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParams;
    }
}
