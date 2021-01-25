package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.mobiledevteam.proderma.cell.AllClinicAdapter;
import com.mobiledevteam.proderma.cell.AllProductAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.HomeProduct;

import java.util.ArrayList;

public class AllProductActivity extends AppCompatActivity {
    private GridView _allGridView;
    ArrayList<HomeProduct> mAllProductList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        _allGridView = (GridView)findViewById(R.id.grid_allProduct);
        getData();
        _allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), OneProductActivity.class).putExtra("product_id", mAllProductList.get(position).getmId());
                startActivity(intent);
            }
        });
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
                    .load(Common.getInstance().getBaseURL()+"api/getProductsInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
//                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonArray products_array = result.get("productsInfo").getAsJsonArray();
                                for(JsonElement productElement : products_array){
                                    JsonObject theproduct = productElement.getAsJsonObject();
                                    String id = theproduct.get("id").getAsString();
                                    String name = theproduct.get("name").getAsString();
                                    String price = theproduct.get("price").getAsString();
                                    String image = theproduct.get("photo").getAsString();
                                    String description = theproduct.get("information").getAsString();
                                    mAllProductList.add(new HomeProduct(id,name,price,image,description));
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
        AllProductAdapter adapter_clinic = new AllProductAdapter(getBaseContext(), mAllProductList);
        _allGridView.setAdapter(adapter_clinic);
    }
}