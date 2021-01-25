package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.R;

import java.util.ArrayList;
import java.util.List;

public class AllProductAdapter extends ArrayAdapter<HomeProduct> {
    private Context mContext;
    private List<HomeProduct> allProductList = new ArrayList<>();

    public AllProductAdapter(Context context, ArrayList<HomeProduct> list) {
        super(context, 0 , list);
        mContext = context;
        allProductList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_all_product,parent,false);

        HomeProduct currentProduct = allProductList.get(position);
//
        ImageView product_image = (ImageView) listItem.findViewById(R.id.img_product);
        Ion.with(mContext).load(currentProduct.getmImage()).intoImageView(product_image);
        TextView name = (TextView) listItem.findViewById(R.id.name_product);
        name.setText(currentProduct.getmName());
        return listItem;
    }
}