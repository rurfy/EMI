package com.example.emi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class ShowTicket extends AppCompatActivity {

    Button buttonEdit;
    Button buttonBack;
    EditText inputTitle;
    EditText inputCreator;
    EditText inputProblem;
    TextView textViewStatus;
    LinearLayout categorieLayout;
    ArrayList<Integer> ticketId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_ticket);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            ticketId.add(0, b.getInt("key"));
        } else {
            ticketId.add(0, 1);
        }

        inputProblem = (EditText) findViewById(R.id.textInputEditTextProblem);
        inputTitle = (EditText) findViewById(R.id.textInputEditTextTitle);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        categorieLayout = (LinearLayout) findViewById(R.id.linLayCheckBoxes);


        // Buttons Bezeichnung zuweisen
        buttonEdit = (Button) findViewById(R.id.buttonLeft);
        buttonEdit.setText(R.string.edit);
        buttonBack = (Button) findViewById(R.id.buttonRight);
        buttonBack.setText(R.string.back);


        APIConnector.getOne("Ticket", null, ticketId.get(0), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                super.onSuccess(statusCode, headers, response);
                HashMap<String, String> ticketDataMap = Utils.jsonToArrayListHash(response).get(0);

                TicketController.setStaticContent(inputTitle, inputProblem, ticketDataMap, false);
                TicketController.setStatus(textViewStatus, ticketDataMap);

                APIConnector.get("Ticket_hat_Kategorie/" + ticketId.get(0) + "/TicketID", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        super.onSuccess(statusCode, headers, response);

                        final ArrayList<String> selectedCategories = Utils.jsonToArrayListString(response, "KategorieID");


                        APIConnector.get("Kategorie", null, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {


                                super.onSuccess(statusCode, headers, response);

                                HashMap<String, String> categoriesHashMap = Utils.jsonArraytoHashMap(response);

                                TicketController.setSelectedCategoriesTextView(categorieLayout, ShowTicket.this, categoriesHashMap, selectedCategories);
                            }
                        });
                    }
                });
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toEditPage = new Intent(ShowTicket.this, EditTicket.class);
                Bundle b = new Bundle();
                b.putInt("key", ticketId.get(0));
                toEditPage.putExtras(b);
                startActivity(toEditPage);
                finish();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Ziel anpassen
                Intent toAllTicketsPage = new Intent(ShowTicket.this, MenuController.class);
                startActivity(toAllTicketsPage);
            }
        });
    }
}