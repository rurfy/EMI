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

        buttonEdit = (Button) findViewById(R.id.buttonLeft);
        buttonEdit.setText(R.string.edit);
        buttonBack = (Button) findViewById(R.id.buttonRight);
        buttonBack.setText(R.string.back);

        // Testmap
        Map ticketData = new HashMap();
        ticketData.put("Titel", "TestTitel");
        ticketData.put("Ersteller", "Marie Berger");
        ticketData.put("Problembeschreibung", "ist kaputt");
        ticketData.put("Kategorie", "2");

        // TODO Daten aus DB holen;


        // Werte aus der DB in die Felder eintragen und Felder sperren
        inputTitle = (EditText)findViewById(R.id.textInputEditTextTitle);
        inputTitle.setEnabled(false);
        inputTitle.setText(ticketData.get("Titel").toString());

        inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
        inputCreator.setEnabled(false);
        inputCreator.setText(ticketData.get("Ersteller").toString());

        inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);
        inputProblem.setEnabled(false);
        inputProblem.setText(ticketData.get("Problembeschreibung").toString());




        // TODO getKategories() -> String[][]
        final String[][] test2 = new String[3][2];
        test2[0][0] = "2";
        test2[0][1] = "test";
        test2[1][0] = "5";
        test2[1][1] = "test2";

        List<String> test = new ArrayList<String>();

        for (int i = 0; i<test2.length; i++) {

            if (test2[i][1] == null) {
                break;
            }else {
                test.add(test2[i][1]);
            }
        }
        // Drop-Down-Liste füllen
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, test);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setEnabled(false);
        spinnerCategory.setClickable(false);
        // Wert aus DB in DropDown auswählen
        for (int i = 0; i < test2.length; i++) {

            if (test2[i][0].equals((ticketData.get("Kategorie").toString()))) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }
}
