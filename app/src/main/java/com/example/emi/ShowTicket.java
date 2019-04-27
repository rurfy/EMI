package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

public class ShowTicket extends AppCompatActivity {

    Button buttonEdit;
    Button buttonBack;
    EditText inputTitel;
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

        // TODO Daten aus DB holen;


        // Werte aus der DB in die Felder eintragen und Felder sperren
        inputTitel = (EditText)findViewById(R.id.textInputEditTextTitel);
        inputTitel.setEnabled(false);
        inputTitel.setText(ticketData.get("Titel").toString());

        inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
        inputCreator.setEnabled(false);
        inputCreator.setText(ticketData.get("Ersteller").toString());

        inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);
        inputProblem.setEnabled(false);
        inputProblem.setText(ticketData.get("Problembeschreibung").toString());

        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerCategory.setEnabled(false);
        spinnerCategory.setClickable(false);
        // TODO wie bestehende Kategorie anzeigen?



    }
}
