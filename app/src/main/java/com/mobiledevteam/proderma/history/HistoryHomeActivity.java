package com.mobiledevteam.proderma.history;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.mobiledevteam.proderma.cell.AllEventAdapter;
import com.mobiledevteam.proderma.cell.AllOrderHistroyAdapter;
import com.mobiledevteam.proderma.cell.Event;
import com.mobiledevteam.proderma.cell.OrderHistory;

import java.util.ArrayList;

public class HistoryHomeActivity extends AppCompatActivity {
    private GridView _allGridView;
    ArrayList<OrderHistory> mAllOrderList = new ArrayList<>();
    private String mClinicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_home);
        mClinicID = Common.getInstance().getClinicID();
        _allGridView = (GridView)findViewById(R.id.grid_allhistory);
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
        json.addProperty("id", mClinicID);
        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getOrderHistoryInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result:::", result.toString());
                            if (result != null) {
                                JsonArray orders_array = result.get("ordersInfo").getAsJsonArray();
                                for(JsonElement orderElement : orders_array){
                                    JsonObject theorder = orderElement.getAsJsonObject();
                                    String id = theorder.get("id").getAsString();
                                    String productname = theorder.get("productname").getAsString();
                                    String productimage = theorder.get("photo").getAsString();
                                    String count = theorder.get("count").getAsString();
                                    String extra = theorder.get("extra").getAsString();
                                    String orderdate = theorder.get("date").getAsString();
                                    String status = theorder.get("status").getAsString();
                                    mAllOrderList.add(new OrderHistory(id,productname,productimage,count,extra,orderdate,status));
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
        AllOrderHistroyAdapter adapter_order = new AllOrderHistroyAdapter(getBaseContext(), mAllOrderList);
        _allGridView.setAdapter(adapter_order);
    }
}