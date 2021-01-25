package com.mobiledevteam.proderma.event;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.AllClinicAdapter;
import com.mobiledevteam.proderma.cell.AllEventAdapter;
import com.mobiledevteam.proderma.cell.Event;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.home.OneClinicActivity;

import java.util.ArrayList;

public class EventHomeActivity extends AppCompatActivity {
    private GridView _allGridView;
    ArrayList<Event> mAllEventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_home);
        _allGridView = (GridView)findViewById(R.id.grid_allEvent);
        _allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), OneClinicActivity.class).putExtra("clinic_id", mAllEventList.get(position).getmId());
//                startActivity(intent);
            }
        });
        getData();
    }
    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("email", "");
        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getEventsInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            if (result != null) {
                                JsonArray events_array = result.get("eventsInfo").getAsJsonArray();
                                for(JsonElement eventElement : events_array){
                                    JsonObject theevent = eventElement.getAsJsonObject();
                                    String id = theevent.get("id").getAsString();
                                    String title = theevent.get("title").getAsString();
                                    String image = theevent.get("photo").getAsString();
                                    String description = theevent.get("description").getAsString();
                                    mAllEventList.add(new Event(id,title,description,image));
                                }
                                initView();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void initView(){
        AllEventAdapter adapter_event = new AllEventAdapter(getBaseContext(), mAllEventList);
        _allGridView.setAdapter(adapter_event);
    }
}