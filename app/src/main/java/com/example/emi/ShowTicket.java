package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

        //#########################################################

        // Buttons Bezeichnung zuweisen
        buttonEdit = (Button) findViewById(R.id.buttonLeft);
        buttonEdit.setText(R.string.edit);
        buttonBack = (Button) findViewById(R.id.buttonRight);
        buttonBack.setText(R.string.back);


        // TODO getTicket() -> Hashmap


        // Werte aus der DB in die Felder eintragen und Felder sperren
        inputTitle = (EditText)findViewById(R.id.textInputEditTextTitle);
        inputTitle.setEnabled(false);
        inputTitle.setText(ticketDataMap.get("Titel").toString());

        inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
        inputCreator.setEnabled(false);
        inputCreator.setText(ticketDataMap.get("Ersteller").toString());

        inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);
        inputProblem.setEnabled(false);
        inputProblem.setText(ticketDataMap.get("Problembeschreibung").toString());


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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setEnabled(false);
        spinnerCategory.setClickable(false);

        // Wert aus DB in DropDown auswählen
        for (int i = 0; i < categoriesArray.length; i++) {
            if (categoriesArray[i][0].equals((ticketDataMap.get("Kategorie").toString()))) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        // TODO Buttons Funktionalität
    }
}
