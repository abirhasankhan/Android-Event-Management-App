package edu.ewubd.cse489_fall22_abir013;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.GLUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpcomingEventActivity extends AppCompatActivity {
    ListView lvEvents;
    ArrayList<Event> events;
    Button create, history, exit;
    CustomEventAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_landing_page);

        create = findViewById(R.id.btnCreateNew);
        history = findViewById(R.id.btnHistory);
        exit = findViewById(R.id.btnExit);

        events = new ArrayList<>();

        // initialize list-reference by ListView object defined in XML
        lvEvents = findViewById(R.id.lvEvents);
// load events from database if there is any

        create.setOnClickListener(view -> {
            Intent i = new Intent(this,EventInformation.class);
            startActivity(i);
        });

        history.setOnClickListener(view -> {
            System.out.println("History clicked");
        });

        exit.setOnClickListener(view -> {
            System.out.println("exit");
        });

        String[] keys = {"action", "id", "semester"};
        String[] values = {"restore", "2019-1-60-013", "2022-3"};
        httpRequest(keys, values);
//        loadData();
    }


    @SuppressLint("StaticFieldLeak")
    private void httpRequest (final String keys[], final String values[]){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... param) {
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    for (int i = 0; i < keys.length; i++) {
                        params.add(new BasicNameValuePair(keys[i], values[i]));
                    }
                    String URL = "https://muthosoft.com/univ/cse489/index.php";
                    String data = JSONParser.getInstance().makeHttpRequest(URL, "POST", params);
                    return data;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String data) {
                if (data != null) {
                    try {
                        updateEventListByServerData(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }.execute();
    }

    public void updateEventListByServerData(String data) throws JSONException {
        JSONObject jo = new JSONObject(data);
        if(jo.has("events")){
            JSONArray ja = jo.getJSONArray("events");
            System.out.println(ja);
            for(int j=0; j<ja.length(); j++){
                JSONObject event = ja.getJSONObject(j);

                String eventKey = event.getString("key");
                System.out.println(eventKey);

                String eventValue = event.getString("value");



                String[] fieldValues = eventValue.split("-::-");
                System.out.println("Before");
                System.out.println(Arrays.toString(fieldValues));
                System.out.println("After");
                if (fieldValues.length == 9) {
                    String name = fieldValues[0];
                    String place = fieldValues[1];
                    String dateTime = fieldValues[2];
                    String capacity = fieldValues[3];
                    String budget = fieldValues[4];
                    String email = fieldValues[5];
                    String phone = fieldValues[6];
                    String description = fieldValues[7];
                    String eventType = fieldValues[8];
                    Event e = new Event(eventKey, name, place, dateTime, capacity, budget, email, phone, description, eventType);
                    events.add(e);
                }
                CustomEventAdapter adapter = new CustomEventAdapter(this, events);
                lvEvents.setAdapter(adapter);

                // handle the click on an event-list item
                lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        // String item = (String) parent.getItemAtPosition(position);
                        System.out.println(position);
                        Intent i = new Intent(UpcomingEventActivity.this, EventInformation.class);
                        Bundle mBundle = new Bundle();
                        i.putExtra("EventKey", events.get(position).key);

                        //Event Data
                        mBundle.putString("name", events.get(position).getName());
                        //System.out.println(events.get(position).getName());
                        mBundle.putString("place", events.get(position).getPlace());
                       //System.out.println(events.get(position).getPlace());
                        mBundle.putString("eventType", events.get(position).getEventType());
                        //System.out.println(events.get(position).getEventType());
                        mBundle.putString("datetime", events.get(position).getDatetime());
                        //System.out.println(events.get(position).getDatetime());
                        mBundle.putString("capacity", events.get(position).getCapacity());
                        //System.out.println(events.get(position).getCapacity());
                        mBundle.putString("budget", events.get(position).getBudget());
                        //System.out.println(events.get(position).getBudget());
                        mBundle.putString("email", events.get(position).getEmail());
                        //System.out.println(events.get(position).getEmail());
                        mBundle.putString("phone", events.get(position).getPhone());
                        //System.out.println(events.get(position).getPhone());
                        mBundle.putString("des", events.get(position).getDescription());
                        //System.out.println(events.get(position).getDescription());

                        i.putExtras(mBundle);

                        startActivity(i);

                    }
                });
                // handle the long-click on an event-list item
                lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        String message = "Do you want to delete event - "+events.get(position).name +" ?";
                        System.out.println(message);
                        showDialog(message, "Delete Event", events.get(position).key);
                        return true;
                    }
                });

            }
        } else{
            System.out.println(jo);
        }
    }
/*
    private void loadData(){
        events = new ArrayList<>();
        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        if (rows.getCount() == 0) {
            return;
        }
        //events = new Event[rows.getCount()];
        while (rows.moveToNext()) {
            String key = rows.getString(0);
            String eventData = rows.getString(1);
            String[] fieldValues = eventData.split("-::-");
            if (fieldValues.length >= 9) {
                String name = fieldValues[0];
                String place = fieldValues[1];
                String dateTime = fieldValues[2];
                String capacity = fieldValues[3];
                String budget = fieldValues[4];
                String email = fieldValues[5];
                String phone = fieldValues[6];
                String description = fieldValues[7];
                String eventType = fieldValues[8];
                Event e = new Event(key, name, place, dateTime, capacity, budget, email, phone, description, eventType);
                events.add(e);
            }
        }
        db.close();
        CustomEventAdapter adapter = new CustomEventAdapter(this, events);
        lvEvents.setAdapter(adapter);

        // handle the click on an event-list item
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // String item = (String) parent.getItemAtPosition(position);
                System.out.println(position);
                Intent i = new Intent(UpcomingEventActivity.this, EventInformation.class);
                i.putExtra("EventKey", events.get(position).key);


                startActivity(i);

            }
        });
        // handle the long-click on an event-list item
        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String message = "Do you want to delete event - "+events[position].name +" ?";
                String message = "Do you want to delete event - "+events.get(position).name +" ?";
                System.out.println(message);
                showDialog(message, "Delete Event", events.get(position).key);
                return true;
            }
        });

    }*/

    private void showDialog(String message, String title, String key){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        showMessage("Remote database has no delete option");
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alart = builder.create();

        alart.show();

    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
