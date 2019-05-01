package com.example.emi;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TicketController {

    // Setzt den Inhalt und die Bearbeitbarkeit für die InputFelder (Titel, Problembeschreibung)
    // Verwendung: ShowTicket, EditTicket
    public static void setStaticContent(EditText inputTitle, EditText inputProblem, HashMap<String, String> ticketDataMap, boolean enabled){


        inputTitle.setText(ticketDataMap.get("Titel").toString());
        inputTitle.setEnabled(enabled);

        //inputCreator.setText(ticketDataMap.get("Ersteller").toString());
        //inputCreator.setEnabled(enabled);

        inputProblem.setText(ticketDataMap.get("Problembeschreibung").toString());
        inputProblem.setEnabled(enabled);
    }


    // Befüllt den Spinner für den Status
    // Verwendung: EditTicket, CreateTicket
    public static void setDropDownStatusContent (Spinner spinnerStatus, Context context, ArrayList<HashMap<String, String>> statusList) {

        List<String> spinnerArray =  new ArrayList<>();

        spinnerArray.add(statusList.get(0).get("Bezeichnung"));
        spinnerArray.add(statusList.get(1).get("Bezeichnung"));
        spinnerArray.add(statusList.get(2).get("Bezeichnung"));

        // Erstellen des Adapters, der Den DropDownKontent enthält
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setVisibility(View.VISIBLE);
    }


    // Setzt die Farbe des Status-Kästchens (in ShowTicket verwendet)
    // Verwendung: ShowTicket
    public static void setStatus (TextView textViewStatus,  HashMap<String, String> ticketDataMap) {

        switch (Integer.parseInt(ticketDataMap.get("StatusID").toString())) {
            case 10: {
                textViewStatus.setBackgroundResource(R.drawable.circle_red);
                break;
            }
            case 50: {
                textViewStatus.setBackgroundResource(R.drawable.circle_yellow);
                break;
            }
            case 100: {
                textViewStatus.setBackgroundResource(R.drawable.circle_green);
                break;
            }
            default: {
                textViewStatus.setBackgroundResource(R.drawable.circle_red);
                break;
            }
        }
    }


    // Wählt den, aus dem Ticket ausgelesenen, Statuswert im Spinner aus
    // Verwendung: EditTicket
    public static void setSelectedStatus (Spinner spinnerStatus, HashMap<String, String> ticketDataMap, ArrayList<HashMap<String, String>> statusList) {

        String status = "";
        // Zuordnen der StatusID zu einem Status
        for (int i = 0; i < statusList.size(); i++) {
            if (ticketDataMap.get("StatusID").toString().equals(statusList.get(i).get("ID"))) {
                status = statusList.get(i).get("Bezeichnung");
            }
        }

        // Auswählen des Statuses aus der DropDownListe
        ArrayAdapter adapter = (ArrayAdapter) spinnerStatus.getAdapter();
        int pos = adapter.getPosition(status);
        spinnerStatus.setSelection(pos);
    }


    // Erstellt CheckBoxen für alle vorhandenen Kategorien
    // Verwendung: CreateTicket
    // HashMap: Key -> KategorieID, Value -> Bezeichnung
    public static ArrayList <CheckBox> setAllCategories (LinearLayout checkBoxContainer, Context context, HashMap<String, String> categoriesHashMap) {

        ArrayList<CheckBox> checkBoxesAllCategories = new ArrayList<>();

        // Erstellen einer ChaeckBox für jede Kategorie
        int i = 0;
        Iterator it = categoriesHashMap.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();
            CheckBox checkBox = new CheckBox(context);
            checkBox.setId(Integer.parseInt(pair.getKey().toString()));
            checkBox.setText(pair.getValue().toString());
            checkBox.setTag(pair.getValue().toString());

            // Dient später dem Auslesen der Werte der Buttons, wir an getSelectedCategories() in der onClick() weitergegeben
            checkBoxesAllCategories.add(i, checkBox);
            // Hinzufügen der CheckBox zum LinearLayout des ScrollViews
            checkBoxContainer.addView(checkBox);
            i++;
        }
        return checkBoxesAllCategories;
    }


    // Erstellt Checkboxen für alle vorhandenen Kategorien und setzt Häckchen in die Checkboxen der Kategorien, die zu einem Ticket gehören
    // Verwendung: EditTicket
    public static ArrayList <CheckBox> setSelectedCategoriesCheckBox (LinearLayout checkBoxContainer, Context context, HashMap<String, String> categoriesHashMap, ArrayList<String> selectedCategories) {

        ArrayList<CheckBox> checkBoxesAllCategories = new ArrayList<>();

        // Erstellen einer ChaeckBox für jede Kategorie
        int i = 0;
        Iterator it = categoriesHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            CheckBox checkBox = new CheckBox(context);
            checkBox.setId(Integer.parseInt(pair.getKey().toString()));
            checkBox.setText(pair.getValue().toString());
            checkBox.setTag(pair.getValue().toString());

            // Abfragen, ob das Ticket diese Kategorie enhält, wenn ja wird ein Häckchen gesetzt
            for (int j = 0; j < selectedCategories.size(); j++) {
                if (pair.getKey().toString().equals(selectedCategories.get(j))) {
                    checkBox.setChecked(true);
                }
            }

            // Dient später dem Auslesen der Werte der Buttons, wir an getSelectedCategories() in der onClick() weitergegeben
            checkBoxesAllCategories.add(i, checkBox);
            // Hinzufügen der CheckBox zum LinearLayout des ScrollViews
            checkBoxContainer.addView(checkBox);
            i++;
        }
        return checkBoxesAllCategories;
    }


    // Zeigt alle Kategorien eines Tickets als Liste von TextViews an
    // Verwendung ShowTicket
    public static void setSelectedCategoriesTextView (LinearLayout categorieLayout, Context context, HashMap<String, String> categoriesHashMap, ArrayList<String> selectedCategories) {

        ArrayList<TextView> createdCategorieTextViews = new ArrayList<>();
        List<String> selectedCategoriesList = new ArrayList<>();

        // selectedCategories besitzt die ID's der zum Ticket gehörigen Kategorien
        // categoriesHashMap enhält alle Kategorien, Key -> ID, Value -> Bezeichnung
        for (int i = 0; i<selectedCategories.size(); i++) {

            selectedCategoriesList.add(categoriesHashMap.get(selectedCategories.get(i)));
        }

        // Erstellen eines TextViews für jede zum Ticket gehörige Kategorie
        for (int i = 0; i < selectedCategoriesList.size(); i++) {
            TextView selectedCategoryTextView = new TextView(context);
            selectedCategoryTextView.setText(selectedCategoriesList.get(i));
            selectedCategoryTextView.setTextColor(Color.parseColor("#000000"));

            createdCategorieTextViews.add(i, selectedCategoryTextView);

            // Hinzufügen der TextViews zum LinearLayout des ScrollViews
            categorieLayout.addView(selectedCategoryTextView);

        }
    }


    // Holt die vom User eingegebenen Werte aus den InputFeldern (Titel, Problembeschreibung)
    // Verwendung: EditTicket, CreateTicket
    public static HashMap <String, String> getStaticContent (EditText inputTitle, EditText inputProblem, Context context) {

        HashMap <String, String> ticketDataMap = new HashMap<>();

        // Holen des aktuellen Datums
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date currentDate = new Date();
        String date = formatter.format(currentDate);


            // Schreiben der Daten in eine Hashmap für Post-Methode
            ticketDataMap.put("Titel", inputTitle.getText().toString());
            //ticketDataMap.put("Ersteller", inputCreator.getText().toString());
            ticketDataMap.put("Problembeschreibung", inputProblem.getText().toString());
            ticketDataMap.put("Datum", date);

        return ticketDataMap;

    }


    // Holt den vom User ausgewählten Wert aus dem Status-Spinner
    // Verwendung: EditTicket, CreateTicket
    public static String getStatus (Spinner spinnerStatus, ArrayList<HashMap<String, String>> statusList) {

        String status = spinnerStatus.getSelectedItem().toString();
        String statID = "";
        for (int i = 0; i < statusList.size(); i++) {
            if (status.equals(statusList.get(i).get("Bezeichnung"))) {
                statID = statusList.get(i).get("ID");
            }
        }
        return statID;
    }


    // Holt die vom User ausgewählten Kategorien
    // Die ArrayList mit den CheckBoxen erhält man nach dem Generieren der CheckBoxen aus der Methode setSelectedCategoriesCheckBox() oder setAllCategories()
    // Verwendung: EditTicket, CreateTicket
    public static ArrayList <String> getSelectedCategories (ArrayList <CheckBox> checkBoxesArrayList) {

        ArrayList<String> selectedCategories = new ArrayList<>();
        int pos = 0;
        for (int j = 0; j < checkBoxesArrayList.size(); j++) {
            if (checkBoxesArrayList.get(j).isChecked() == true) {
                selectedCategories.add(pos, Integer.toString(checkBoxesArrayList.get(j).getId()));
                pos++;
            }
        }
        return selectedCategories;
    }
}
