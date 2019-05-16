package com.example.emi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emi.R;
import com.example.emi.model.OnJSONResponseCallback;
import com.example.emi.model.RestUtils;
import com.example.emi.view.LayoutUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import static com.loopj.android.http.AsyncHttpClient.log;

public class AllTicketsController extends AppCompatActivity {

    ArrayList<HashMap<String, String>> list;
    ArrayAdapter<String> list2;
    ListView lvAllTickets;
    ArrayList<HashMap<String,String>> statusList;
    Spinner filterTypes;
    Spinner categories;
    String selectedCategory;
    EditText secondCategoryText;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tickets);

        lvAllTickets = (ListView) findViewById(R.id.ticketList);
        ImageView back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        TextView title = (TextView) findViewById(R.id.viewCaption);
        title.setText(R.string.allTickets);

        categories = (Spinner) findViewById(R.id.filterTypes);
        filterTypes = (Spinner) findViewById(R.id.filterTypes2);
        secondCategoryText = (EditText) findViewById(R.id.secondCategoryText);
        Button searchButton = (Button) findViewById(R.id.searchButton);

        ImageView house = findViewById(R.id.home);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(AllTicketsController.this, MenuController.class);
                startActivity(backToHome);
            }
        });

        lvAllTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toShowTicket = new Intent(AllTicketsController.this, ShowTicketController.class);
                toShowTicket.putExtra("key", Integer.parseInt(list.get(position).get("ID")));
                startActivity(toShowTicket);
            }
        });


        RestUtils.getAllItems("Ticket", AllTicketsController.this, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {
                list = JSONUtils.jsonToArrayListHash(response);
                //list2 = JSONUtils.jsonToArrayListString(response);
                adapter = new ArrayAdapter<String>(AllTicketsController.this, R.layout.all_tickets_item, R.id.ticketList, new String[]{"ID","Titel", "Datum"});

                //adapter = new SimpleAdapter(AllTicketsController.this, list, R.layout.all_tickets_item,
                //        new String[]{"ID", "Titel", "Datum"}, new int[]{R.id.itemID, R.id.itemTitle, R.id.itemDate}){
                /*    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View row = super.getView(position,convertView,parent);

                        if(row==null){
                            LayoutInflater inflater=getLayoutInflater();
                            row=inflater.inflate(R.layout.all_tickets_item, parent, false);
                        }

                        TextView label=(TextView)row.findViewById(R.id.itemStatus);

                        if(list.get(position).get("StatusID").equals("10")){
                            label.setBackgroundResource(R.drawable.circle_red);
                        }else if(list.get(position).get("StatusID").equals("50")){
                            label.setBackgroundResource(R.drawable.circle_yellow);
                        }else{
                            label.setBackgroundResource(R.drawable.circle_green);
                        }
                        return row;
                    }
                };
                lvAllTickets.setAdapter(adapter);

                secondCategoryText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AllTicketsController.this.adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }*/

            @Override
            public void onJSONResponse(String id) {

            }
        });

        //Status aus der DB lesen und sowohl in den Spinner, als auch in eine ArrayList schreiben
        RestUtils.getAllItems("Status", AllTicketsController.this, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(JSONArray response) {

                statusList = JSONUtils.jsonToArrayListHash(response);
                LayoutUtils.setDropDownStatusContent(filterTypes, AllTicketsController.this, statusList);
            }

            //Unwichtig, da man immer ein Array bekommt
            //Der Fall, dass sich das ändern könnte, sollte jedoch behandelt werden
            @Override
            public void onJSONResponse(String id) {

            }
        });

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.getSelectedItem().toString();

                if(selectedCategory.equals("Status")){
                   if(filterTypes.getVisibility()==View.INVISIBLE){
                       filterTypes.setVisibility(View.VISIBLE);
                       secondCategoryText.setVisibility(View.INVISIBLE);
                   }
                }else{
                    if(filterTypes.getVisibility()==View.VISIBLE){
                        filterTypes.setVisibility(View.INVISIBLE);
                        secondCategoryText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(secondCategoryText.getVisibility()==View.VISIBLE){
                    if(secondCategoryText.getText().toString().equals("")) {
                        Toast.makeText(AllTicketsController.this, "Bitte füllen Sie die Suchleiste aus", Toast.LENGTH_SHORT).show();
                    }else{

                    }
                }else{

                }

            }
        });
    }
}