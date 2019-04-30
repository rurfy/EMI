package com.example.emi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class MenuController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.main_menu);

        Button createTicket = findViewById(R.id.createTicketButton);
        Button allTickets = findViewById(R.id.viewAllTicketsButton);

        createTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RestUsage.getAllItems("Ticket", MenuController.this);

                //Beispielhafte Verwendung der Get-Methode für die Ticket-Tabelle
                //Das Interface wird implementiert, indem eine neue Instanz erzeugt wird
                //Seine Klassen müssen zwingend überschrieben werden, aber nicht alle müsssen genutzt werden
                RestUsage.getAllItems("Ticket", new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(JSONArray response) {
                            //Das JSONAraay wird per Interface gezogen und beispielhaft in eine lokale ArrayList geschrieben
                            //Hierfür wurde die Utilsklasse zur Hilfe genommen
                            ArrayList<HashMap<String,String>> arrayList = Utils.jsonToArrayList(response);
                            for (int i = 0; i < arrayList.size(); i++) {
                                Log.e("Test123", arrayList.get(i).get("Titel"));
                            }
                    }

                    //Wird nicht benötigt hier, muss aber überschrieben werden
                    @Override
                    public void onJSONResponse(JSONObject response) {

                    }
                });
                //RestUsage.postOneItem();

                //Intent toCreatePage = new Intent(MenuController.this, CreateTicket.class);
                //startActivity(toCreatePage);

            }
        });

        allTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toAllTicketsPage = new Intent(MenuController.this, CreateTicket.class);
                startActivity(toAllTicketsPage);

            }
        });

    }

}
