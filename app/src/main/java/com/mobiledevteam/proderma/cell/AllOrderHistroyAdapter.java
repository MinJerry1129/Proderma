package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.R;

import java.util.ArrayList;
import java.util.List;

public class AllOrderHistroyAdapter extends ArrayAdapter<OrderHistory> {
    private Context mContext;
    private List<OrderHistory> allOrderList = new ArrayList<>();

    public AllOrderHistroyAdapter(Context context, ArrayList<OrderHistory> list) {
        super(context, 0 , list);
        mContext = context;
        allOrderList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_all_history,parent,false);

        OrderHistory currentEvent = allOrderList.get(position);
//
        ImageView product_image = (ImageView) listItem.findViewById(R.id.productimage);
        Ion.with(mContext).load(currentEvent.getmProductImage()).intoImageView(product_image);//
        TextView productname = (TextView) listItem.findViewById(R.id.txt_productname);
        productname.setText(currentEvent.getmProductName());
        TextView orderdate = (TextView) listItem.findViewById(R.id.txt_date);
        orderdate.setText(currentEvent.getmDate());
        TextView count = (TextView) listItem.findViewById(R.id.txt_count);
        count.setText(currentEvent.getmCount());
        TextView extra = (TextView) listItem.findViewById(R.id.txt_extra);
        extra.setText(currentEvent.getmExtra());
        return listItem;
    }
}
