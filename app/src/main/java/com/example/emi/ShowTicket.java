package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    //Spinner spinnerCategory;
    TextView textViewStatus;
    LinearLayout categorieLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_ticket);


        inputProblem = (EditText) findViewById(R.id.textInputEditTextProblem);
        inputTitle = (EditText) findViewById(R.id.textInputEditTextTitle);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        categorieLayout = (LinearLayout) findViewById(R.id.linLayCheckBoxes);

        int ticketID = 37;

        // Buttons Bezeichnung zuweisen
        buttonEdit = (Button) findViewById(R.id.buttonLeft);
        buttonEdit.setText(R.string.edit);
        buttonBack = (Button) findViewById(R.id.buttonRight);
        buttonBack.setText(R.string.back);


        APIConnector.getOne("Ticket", null, 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                super.onSuccess(statusCode, headers, response);
                HashMap<String, String> ticketDataMap = Utils.jsonToArrayListHash(response).get(0);

                TicketController.setStaticContent(inputTitle, inputProblem, ticketDataMap, false);
                TicketController.setStatus(textViewStatus, ticketDataMap);

                APIConnector.get("Ticket_hat_Kategorie/1/TicketID", null, new JsonHttpResponseHandler() {
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
    }
}