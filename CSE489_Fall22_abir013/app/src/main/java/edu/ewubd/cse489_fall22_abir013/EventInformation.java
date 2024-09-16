package edu.ewubd.cse489_fall22_abir013;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventInformation extends AppCompatActivity {

    EditText EditTname, EditTplace, EditTdateAndTime, EditTcapacity, EditTbudget, EditTemail, EditTphone,EditTdes;
    Button save, share, cancel;
    RadioButton indoor, outDoor, online;
    private String key = "";
    Intent i;
    @SuppressLint("MissingInFlatID")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        i = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);

        EditTname = findViewById(R.id.EditTname);
        EditTplace = findViewById(R.id.EditTplace);
        EditTdateAndTime = findViewById(R.id.EditTdateAndTime);
        EditTcapacity = findViewById(R.id.EditTcapacity);
        EditTbudget = findViewById(R.id.EditTbudget);
        EditTemail = findViewById(R.id.EditTemail);
        EditTphone = findViewById(R.id.EditTphone);
        EditTdes = findViewById(R.id.EditTdes);

        indoor = findViewById(R.id.indoor);
        outDoor = findViewById(R.id.outdoor);
        online = findViewById(R.id.online);

        save = findViewById(R.id.btnSave);
        share = findViewById(R.id.btnShare);
        cancel = findViewById(R.id.btnCancel);

        indoor.setOnClickListener(view -> funIndoor());
        outDoor.setOnClickListener(view -> funOutDoor());
        online.setOnClickListener(view -> funOnline());

        save.setOnClickListener(view -> funSave());
        share.setOnClickListener(view -> funShare());
        cancel.setOnClickListener(view -> funCancel());



        if (i.hasExtra("EventKey")){
            key = i.getStringExtra("EventKey");
            System.out.println(key);

            Bundle extras = i.getExtras();

            String name = extras.getString("name");;
            String place = extras.getString("place");
            String type = extras.getString("eventType");
            String dateTime = extras.getString("datetime");
            String capacity = extras.getString("capacity");
            String budget = extras.getString("budget");
            String email = extras.getString("email");
            String phone = extras.getString("phone");
            String des = extras.getString("des");


            switch (type) {
                case "Indoor":
                    indoor.setChecked(true);
                    break;
                case "Outdoor":
                    outDoor.setChecked(true);
                    break;
                case "Online":
                    online.setChecked(true);
                    break;
            }

            System.out.println(name + "-::-" + place + "-::-" + type + "-::-" + dateTime + "-::-" + capacity + "-::-" + budget + "-::-" + email + "-::-" + phone + "-::-" + des);


            EditTname.setText(name);
            EditTplace.setText(place);
            EditTdateAndTime.setText(dateTime);
            EditTcapacity.setText(capacity);
            EditTbudget.setText(budget);
            EditTemail.setText(email);
            EditTphone.setText(phone);
            EditTdes.setText(des);




        }

    }



    private void initializeFormWithExistingData(String key) {

        //i = getIntent();



        //System.out.println(eventType);
/*
        if(eventType.equals("Indoor")){
            indoor.setChecked(true);
        }
        else if (eventType.equals("Outdoor")){
            outDoor.setChecked(true);
        }
        else if (eventType.equals("Online")){
            online.setChecked(true);
        }
*/
    }

    void funIndoor(){
        System.out.println(indoor.getText().toString().trim());
    }
    void funOutDoor(){
        System.out.println(outDoor.getText().toString().trim());
    }
    void funOnline(){
        System.out.println(online.getText().toString().trim());
    }

    public void funSave() {

        String e_name = EditTname.getText().toString().trim();
        String e_place = EditTplace.getText().toString().trim();
        String e_date = EditTdateAndTime.getText().toString().trim();
        String e_capacity = EditTcapacity.getText().toString().trim();
        String e_budget = EditTbudget.getText().toString().trim();
        String e_email = EditTemail.getText().toString().trim();
        String e_phone = EditTphone.getText().toString().trim();
        String e_des = EditTdes.getText().toString().trim();

        String type = "";
        if (indoor.isChecked()) {
            type = "Indoor";
        } else if (outDoor.isChecked()) {
            type = "Outdoor";
        } else if (online.isChecked()) {
            type = "Online";
        }
        if (key.length() == 0) {
            key = e_name + System.currentTimeMillis();
        }

        String value = e_name + "-::-" + e_place + "-::-" + type + "-::-" + e_date + "-::-" + e_capacity + "-::-" + e_budget + "-::-" + e_email + "-::-" + e_phone + "-::-" + e_des;

//        KeyValueDB kvdb = new KeyValueDB(getApplicationContext());
//        kvdb.insertKeyValue(key, value);

        System.out.println("Key is: " + key);

        String[] keys = {"action", "id", "semester", "key", " event"};
        String[] values = {"backup", "2019-1-60-013", "2022-3", key, value};

        httpRequest(keys, values);

    }
    @SuppressLint("StaticFieldLeak")
    private void httpRequest ( final String keys[], final String values[]){
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
                        System.out.println("before");
                        System.out.println(data);
                        Intent i = new Intent(EventInformation.this, UpcomingEventActivity.class);
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }.execute();
    }
/*
    public void updateEventListByServerData(String data) throws JSONException {
        JSONObject jo = new JSONObject(data);
        if(jo.has("events")){
            JSONArray ja = jo.getJSONArray("events");
            for(int j=0; j<ja.length(); j++){
                JSONObject event = ja.getJSONObject(j);
                String eventKey = event.getString("e_key");
                String eventValue = event.getString("e_value");
// split eventValue to show in event list
            }
        } else{
            System.out.println(jo);
        }
    }

*/
    void funShare(){

            System.out.println("Has been share");

    }
    void funCancel(){


            System.out.println("Has been cancel");

    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}