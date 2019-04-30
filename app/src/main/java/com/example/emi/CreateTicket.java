package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class CreateTicket extends AppCompatActivity {

        Button buttonCreate;
        Button buttonCancel;
        EditText inputTitle;
        EditText inputCreator;
        EditText inputProblem;
        Spinner spinnerCategory;
        TextView textViewStatus;
        Spinner spinnerStatus;

        ArrayList<HashMap<String, String>> statusList;
    ArrayList<HashMap<String, String>> categoryList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_ticket);

            buttonCreate = (Button)findViewById(R.id.buttonLeft);
            buttonCreate.setText(R.string.ok);
            buttonCancel = (Button) findViewById(R.id.buttonRight);
            buttonCancel.setText(R.string.cancel);

            APIConnector.get("Status", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    statusList = Utils.jsonToArrayList(response);
                    List<String> spinnerArray =  new ArrayList<>();

                    spinnerArray.add(statusList.get(0).get("Bezeichnung"));
                    spinnerArray.add(statusList.get(1).get("Bezeichnung"));
                    spinnerArray.add(statusList.get(2).get("Bezeichnung"));

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CreateTicket.this, android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
                    spinnerStatus.setAdapter(adapter);

                }
            });

            APIConnector.get("Kategorie", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    categoryList = Utils.jsonToArrayList(response);
                    List<String> spinnerArray =  new ArrayList<>();

                    spinnerArray.add(categoryList.get(0).get("Bezeichnung"));
                    spinnerArray.add(categoryList.get(1).get("Bezeichnung"));
                    spinnerArray.add(categoryList.get(2).get("Bezeichnung"));

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CreateTicket.this, android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
                    spinnerCategory.setAdapter(adapter);

                }
            });


            //Wird ausgeführt wenn der Nutzer den OK-Button drückt
            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Test123", "Es geht weiter");
                    HashMap <String, String> ticketDataMap = new HashMap<>();

                    // Daten aus den Feldern auslesen
                    inputTitle = (EditText)findViewById(R.id.textInputEditTextTitle);
                    //inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
                    inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);

                    //Eingaben auslesen und in String speichern
                    String title = inputTitle.getText().toString();
                    String problem = inputProblem.getText().toString();

                    //aktuelles Datum einlesen und in gewünschtem Format abspeichern
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String datum = format.format(date);

                    //ausgewählten Status herausfinden und die korrespondierende StatusID speichern
                    String status = spinnerStatus.getSelectedItem().toString();
                    String statID = "";
                    for (int i = 0; i < statusList.size(); i++) {
                        if (status.equals(statusList.get(i).get("Bezeichnung"))) {
                            statID = statusList.get(i).get("ID");
                        }
                    }

                    //ausgewählte Kategorie herausfinden und die korrespondierende KategorieID speichern
                    String category = spinnerCategory.getSelectedItem().toString();
                    String catID = "";
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (category.equals(categoryList.get(i).get("Bezeichnung"))) {
                            catID = categoryList.get(i).get("ID");
                        }
                    }


                    //Daten in die Hashmap schreiben
                    ticketDataMap.put("Titel", title);
                    ticketDataMap.put("Datum", datum);
                    ticketDataMap.put("Problembeschreibung", problem);
                    ticketDataMap.put("StatusID", statID);
                    ticketDataMap.put("KategorieID", catID);


                    //Das einzufügende JSONObject wird mit den Daten befüllt und in die entsprechende Tabelle geladen
                    JSONObject postObject = Utils.prepareDataForPost(ticketDataMap);
                    RestUsage.postOneItem(postObject,"Ticket");
                }
            });
        }

}


