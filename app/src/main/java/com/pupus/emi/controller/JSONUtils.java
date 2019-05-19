package com.pupus.emi.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//Diese Klasse enthält universale Konvertierungswerkzeuge
//Hier werden JSONArrays in ArrayLists umgewandelt, etc.
class JSONUtils {

    //Eine gesamte ArrayList<HashMap> aus einem JSONArray erstellen
    public static ArrayList<HashMap<String, String>> jsonToArrayListHash(JSONArray jsonArray) {

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

    //Eine gesamte ArrayList<String> aus einem JSONArray erstellen
    public static ArrayList<String> jsonToArrayListString(JSONArray jsonArray, String attribute) {

        ArrayList<String> arrayList = new ArrayList<>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        arrayList.add(jsonArray.getJSONObject(i).getString(attribute));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        return arrayList;

    }

    //Eine HashMap<String, String> aus einem JSONObject erstellen
    private static HashMap<String, String> jsonObjectToHashMap(JSONObject jsonObject) {
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

    //Eine HashMap<String, String> aus einem JSONArray erstellen
    public static HashMap<String, String> jsonArraytoHashMap (JSONArray jsonArray) {
        HashMap <String, String> hashMap = new HashMap<>();

        for (int i = 0; i<jsonArray.length(); i++) {
            try {
                hashMap.put(jsonArray.getJSONObject(i).getString("ID"), jsonArray.getJSONObject(i).getString("Bezeichnung"));
            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

        return hashMap;
    }

    //Ein JSONObject aus einer HashMap<String, String> erstellen
    public static JSONObject prepareDataForPost(HashMap<String,String> hashMap) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Typ", "INSERT");
            for (String key : hashMap.keySet()) {
                String value = hashMap.get(key);
                jsonParams.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParams;
    }

    //Ein JSONArray aus einer ArrayList<String> erstellen
    public static JSONArray stringArrayToJsonArray(ArrayList<String> categories) {
        JSONArray array = new JSONArray();
        for (String category: categories) {
            array.put(category);
        }
        return array;
    }

}
