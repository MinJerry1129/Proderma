package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.R;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends ArrayAdapter<Result> {
    private Context mContext;
    private List<Result> allResultList = new ArrayList<>();

    public ResultAdapter(Context context, ArrayList<Result> list) {
        super(context, 0 , list);
        mContext = context;
        allResultList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_result,parent,false);

        Result currentProduct = allResultList.get(position);
//
        ImageView product_image = (ImageView) listItem.findViewById(R.id.img_result);
        Ion.with(mContext).load(currentProduct.getmImageurl()).intoImageView(product_image);
        return listItem;
    }
}