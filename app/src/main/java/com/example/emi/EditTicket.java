package com.example.emi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class EditTicket extends AppCompatActivity {

    Button buttonCreate;
    Button buttonCancel;
    EditText inputTitle;
    EditText inputCreator;
    EditText inputProblem;
    CheckBox checkBox;
    Spinner spinnerStatus;
    LinearLayout checkBoxContainer;
    ArrayList<CheckBox> checkBoxesCategories;
    ArrayList<HashMap<String, String>> statusList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_ticket);

        inputTitle = (EditText) findViewById(R.id.textInputEditTextTitle);
        inputProblem = (EditText) findViewById(R.id.textInputEditTextProblem);
        //inputCreator = (EditText)findViewById(R.id.textInputEditTextTitle);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        checkBoxContainer = (LinearLayout) findViewById(R.id.linLayCheckBoxes);


        // Buttons Bezeichnung zuweisen
        buttonCreate = (Button) findViewById(R.id.buttonLeft);
        buttonCreate.setText(R.string.ok);
        buttonCancel = (Button) findViewById(R.id.buttonRight);
        buttonCancel.setText(R.string.cancel);


        APIConnector.get("Status", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                statusList = Utils.jsonToArrayListHash(response);

                TicketController.setDropDownStatusContent(spinnerStatus, EditTicket.this, statusList);


                APIConnector.getOne("Ticket", null, 1, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        super.onSuccess(statusCode, headers, response);
                        HashMap<String, String> ticketDataMap = Utils.jsonToArrayListHash(response).get(0);

                        TicketController.setStaticContent(inputTitle, inputProblem, ticketDataMap, true);
                        TicketController.setSelectedStatus(spinnerStatus, ticketDataMap, statusList);

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


                                        checkBoxesCategories = TicketController.setSelectedCategoriesCheckBox(checkBoxContainer, EditTicket.this, categoriesHashMap, selectedCategories);
                                        int x = checkBoxesCategories.size();
                                        Log.e("Size", Integer.toString(x));

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> ticketDataMap = TicketController.getStaticContent(inputTitle, inputProblem);
                String statID = TicketController.getStatus(spinnerStatus, statusList);
                ticketDataMap.put("StatusID", statID);

                ArrayList<String> selectedCategories = TicketController.getSelectedCategories(checkBoxesCategories);


                //TODO Status
                // TODO Update Ticket in DB

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}