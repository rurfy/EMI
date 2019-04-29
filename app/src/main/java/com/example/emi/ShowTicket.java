package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowTicket extends AppCompatActivity {

    Button buttonEdit;
    Button buttonBack;
    EditText inputTitle;
    EditText inputCreator;
    EditText inputProblem;
    Spinner spinnerCategory;
    TextView textViewStatus;

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


        Map ticketDataMap = new HashMap();
        ticketDataMap.put("Titel", "TestTitel");
        ticketDataMap.put("Ersteller", "Marie Berger");
        ticketDataMap.put("Problembeschreibung", "ist kaputt");
        ticketDataMap.put("Kategorie", "2");
        ticketDataMap.put("StatusID", "50");

        //#########################################################

        // Buttons Bezeichnung zuweisen
        buttonEdit = (Button) findViewById(R.id.buttonLeft);
        buttonEdit.setText(R.string.edit);
        buttonBack = (Button) findViewById(R.id.buttonRight);
        buttonBack.setText(R.string.back);


        // TODO getTicket() -> Hashmap
        RestUsage.getOneItem("Ticket", 1, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {
                //Das JSONAraay wird per Interface gezogen und beispielhaft in eine lokale ArrayList geschrieben
                //Hierfür wurde die Utilsklasse zur Hilfe genommen
                ArrayList<HashMap<String,String>> arrayList = Utils.jsonToArrayList(response);

                Map <String,String> ticketDataMap = arrayList.get(0);

                // Werte aus der DB in die Felder eintragen und Felder sperren
                inputTitle = (EditText)findViewById(R.id.textInputEditTextTitle);
                inputTitle.setEnabled(false);
                inputTitle.setText(ticketDataMap.get("Titel").toString());

                //inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
                //inputCreator.setEnabled(false);
                //inputCreator.setText(ticketDataMap.get("Ersteller").toString());

                inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);
                inputProblem.setEnabled(false);
                inputProblem.setText(ticketDataMap.get("Problembeschreibung").toString());

                textViewStatus = (TextView)findViewById(R.id.textViewStatus);
                switch (Integer.parseInt(ticketDataMap.get("StatusID").toString())) {
                    case 50: {
                        textViewStatus.setBackgroundResource(R.drawable.circle_red);
                        break;
                    }
                    case 100: {
                        textViewStatus.setBackgroundResource(R.drawable.circle_yellow);
                        break;
                    }
                    case 1000: {
                        textViewStatus.setBackgroundResource(R.drawable.circle_green);
                        break;
                    }
                    default: {
                        textViewStatus.setBackgroundResource(R.drawable.circle_red);
                        break;
                    }
                }

            }

            //Wird nicht benötigt hier, muss aber überschrieben werden
            @Override
            public void onJSONResponse(JSONObject response) {

            }
        });






         //Auslesen der Kategorien aus der DB
            RestUsage.getAllItems("Kategorie", new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {
               //Das JSONAraay wird per Interface gezogen und beispielhaft in eine lokale ArrayList geschrieben
               //Hierfür wurde die Utilsklasse zur Hilfe genommen
               String[][] categoriesArray = Utils.jsonArraytoStingArray(response, "Bezeichnung");
               for (int i = 0; i < categoriesArray.length; i++) {
                   Log.e("Bezeichnung", categoriesArray[i][1]);
               }

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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowTicket.this, android.R.layout.simple_spinner_item, categoriesList);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
                spinnerCategory.setEnabled(false);
                spinnerCategory.setClickable(false);
                spinnerCategory.setSelection(1);

                // Wert aus DB in DropDown auswählen
                //for (int i = 0; i < categoriesArray.length; i++) {
                 //   if (categoriesArray[i][0].equals((ticketDataMap.get("Kategorie").toString()))) {
                 //       spinnerCategory.setSelection(i);
                 //       break;
                 //   }
                //}

           }

        //Wird nicht benötigt hier, muss aber überschrieben werden
           @Override
          public void onJSONResponse(JSONObject response) {

           }
        });



        // TODO Buttons Funktionalität
    }
}
