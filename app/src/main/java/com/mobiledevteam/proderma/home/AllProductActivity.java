package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private ArrayList<HomeProduct> mTempProductList;
    private EditText _editSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        _allGridView = (GridView)findViewById(R.id.grid_allProduct);
        _editSearch = findViewById(R.id.editSearch);
        setReady();
        getData();
    }
    private void setReady(){
        _allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), OneProductActivity.class).putExtra("product_id", mTempProductList.get(position).getmId());
                startActivity(intent);
            }
        });
        _editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pattern=_editSearch.getText().toString().trim();
                searchRestaurant(pattern);
            }
        });
        findViewById(R.id.imgSearch_Close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _editSearch.setText("");
                mTempProductList=new ArrayList<HomeProduct>(mAllProductList);
                findViewById(R.id.imgSearch_Close).setVisibility(View.INVISIBLE);
                initView();
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
                                mTempProductList=new ArrayList<HomeProduct>(mAllProductList);
                                initView();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void searchRestaurant(String pattern){
        mTempProductList.clear();
        if(pattern.isEmpty()){
            mTempProductList=new ArrayList<HomeProduct>(mAllProductList);
            findViewById(R.id.imgSearch_Close).setVisibility(View.INVISIBLE);
        }
        else{
            for(HomeProduct theProduct:mAllProductList){
                if(theProduct.getmName().toLowerCase().contains(pattern.toLowerCase())){
                    mTempProductList.add(theProduct);
                }
            }
            findViewById(R.id.imgSearch_Close).setVisibility(View.VISIBLE);
        }
        showProductList();
    }
    private void initView(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                AllProductAdapter adapter_clinic = new AllProductAdapter(getBaseContext(), mAllProductList);
                _allGridView.setAdapter(adapter_clinic);
            }
        });
    }
    private void showProductList(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                AllProductAdapter mAdapter = new AllProductAdapter(getBaseContext(), mTempProductList);
                _allGridView.setAdapter(mAdapter);
            }
        });

    }
}