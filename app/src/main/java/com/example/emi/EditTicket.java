package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTicket extends AppCompatActivity {

        Button buttonCreate;
        Button buttonCancel;
        EditText inputTitel;
        EditText inputCreator;
        EditText inputProblem;
        Spinner spinnerCategory;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_ticket);

            buttonCreate = (Button)findViewById(R.id.buttonLeft);
            buttonCreate.setText(R.string.ok);
            buttonCancel = (Button) findViewById(R.id.buttonRight);
            buttonCancel.setText(R.string.cancel);


            // TODO getKategories() -> String[][]
            final String[][] test2 = new String[3][2];
            test2[0][0] = "2";
            test2[0][1] = "test";
            test2[1][0] = "5";
            test2[1][1] = "test2";

            List<String> test = new ArrayList<String>();

            for (int i = 0; i<test2.length; i++) {

                test.add(test2[i][1]);
            }
            // Drop-Down-Liste füllen
            spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, test);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);


            // TODO getTicket(id) -> Hashmap;
            // Testmap
            Map ticketData = new HashMap();
            ticketData.put("Titel", "TestTitel");
            ticketData.put("Ersteller", "Marie Berger");
            ticketData.put("Problembeschreibung", "ist kaputt");

            // Werte aus der DB in die Felder eintragen
            inputTitel = (EditText)findViewById(R.id.textInputEditTextTitel);
            inputTitel.setText(ticketData.get("Titel").toString());
            inputCreator = (EditText)findViewById(R.id.textInputEditTextTitel);
            inputCreator.setText(ticketData.get("Ersteller").toString());
            inputProblem = (EditText)findViewById(R.id.textInputEditTextTitel);
            inputProblem.setText(ticketData.get("Problembeschreibung").toString());



            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputTitel = (EditText)findViewById(R.id.textInputEditTextTitel);
                    inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
                    inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);

                    int categoryPosition = spinnerCategory.getSelectedItemPosition();
                    String categoryId = test2[categoryPosition][0];


                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                    Date currentDate = new Date();
                    String date = formatter.format(currentDate);
                    Log.d("Date", date);

                    //
                }
            });
        }
    }
