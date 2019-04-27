package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CreateTicket extends AppCompatActivity {

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


            // TODO Kategorie-String aus Datenbank
            // Drop-Down-Liste füllen
            List<String> test = new ArrayList<String>();
            test.add("test1");
            test.add("test2");
            spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, test);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);



            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Daten aus den Feldern auslesen
                    inputTitel = (EditText)findViewById(R.id.textInputEditTextTitel);
                    inputCreator = (EditText)findViewById(R.id.textInputEditTextCreator);
                    inputProblem = (EditText)findViewById(R.id.textInputEditTextProblem);

                }
            });
        }
}


