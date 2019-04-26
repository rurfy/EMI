package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateTicket extends AppCompatActivity {

        Button erstellenButton;
        EditText inputTitel;
        EditText inputErsteller;
        EditText inputProblembeschreibung;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_ticket);

            erstellenButton = (Button)findViewById(R.id.buttonErstellen);
            erstellenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputTitel = (EditText)findViewById(R.id.textInputEditTextTitel);
                    inputErsteller = (EditText)findViewById(R.id.textInputEditTextErsteller);
                    inputProblembeschreibung = (EditText)findViewById(R.id.textInputEditTextProblembeschreibung);

                    //
                }
            });
        }
}


