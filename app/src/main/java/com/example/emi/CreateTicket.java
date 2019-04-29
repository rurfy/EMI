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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTicket extends AppCompatActivity {

        Button buttonCreate;
        Button buttonCancel;
        EditText inputTitle;
        EditText inputCreator;
        EditText inputProblem;
        Spinner spinnerCategory;
        TextView textViewStatus;
        Spinner spinnerStatus;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_ticket);

            //##################### Testdaten #########################

            // TODO getKategories() -> String[][]
            final String[][] categoriesArray = new String[3][2];
            categoriesArray[0][0] = "2";
            categoriesArray[0][1] = "test";
            categoriesArray[1][0] = "5";
            categoriesArray[1][1] = "test2";

            //#########################################################

            buttonCreate = (Button)findViewById(R.id.buttonLeft);
            buttonCreate.setText(R.string.ok);
            buttonCancel = (Button) findViewById(R.id.buttonRight);
            buttonCancel.setText(R.string.cancel);


            // TODO getKategories() -> String[][]
            // Liste für DropDown befüllen
            List<String> categoriesList = new ArrayList<String>();
            for (int i = 0; i<categoriesArray.length; i++) {

                if (categoriesArray[i][1] == null) {
                    break;
                }else {
                    categoriesList.add(categoriesArray[i][1]);
                }
            }
            // Drop-Down-Menü füllen
            spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesList);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);



            // TODO getStatus() -> String[][]
            // Liste für DropDown befüllen
            List<String> statusList = new ArrayList<String>();
            statusList.add("nicht begonnen");
            statusList.add("in Bearbeitung");
            statusList.add("beendet");

            // Drop-Down-Menü füllen
            spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerStatus.setAdapter(statusAdapter);



            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map <String, Object> ticketDataMap = new HashMap<String, Object>();

                    // Daten aus den Feldern auslesen
                    inputTitle = (EditText)findViewById(R.id.textInputEditTextTitle);
                    inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
                    inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);

                    // Position der ID aus der DB zuordnen
                    int categoryPosition = spinnerCategory.getSelectedItemPosition();
                    String categoryId = categoriesArray[categoryPosition][0];

                    String status = spinnerStatus.getSelectedItem().toString();
                    switch (status) {
                        case "nicht begonnen": {
                            ticketDataMap.put("StatusID", "50");
                            break;
                        }
                        case "in Bearbeitung": {
                            ticketDataMap.put("StatusID", "100");
                            break;
                        }
                        case "beendet": {
                            ticketDataMap.put("StatusID", "1000");
                            break;
                        }
                    }

                    // aktuelles Datum holen
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                    Date currentDate = new Date();
                    String date = formatter.format(currentDate);


                    ticketDataMap.put("Titel", inputTitle);
                    //ticketDataMap.put("Ersteller", inputCreator);
                    ticketDataMap.put("Problembeschreibung", inputProblem);
                    ticketDataMap.put("Datum", date);

                    // TODO DB Kategorie
                    //ticketDataMap.put("Kategorie", categoryId);

                    // TODO setTicket(ticketDataMap) -> boolean



                }
            });
        }
}


