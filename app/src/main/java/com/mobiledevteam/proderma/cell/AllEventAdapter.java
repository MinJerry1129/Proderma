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

public class AllEventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private List<Event> allEventList = new ArrayList<>();

    public AllEventAdapter(Context context, ArrayList<Event> list) {
        super(context, 0 , list);
        mContext = context;
        allEventList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_all_event,parent,false);

        Event currentEvent = allEventList.get(position);
//
        ImageView clinic_image = (ImageView) listItem.findViewById(R.id.eventimage);
        Ion.with(mContext).load(currentEvent.getmImage()).intoImageView(clinic_image);//
        TextView title = (TextView) listItem.findViewById(R.id.txt_eventTitle);
        title.setText(currentEvent.getmTitle());
        TextView description = (TextView) listItem.findViewById(R.id.txt_description);
        description.setText(currentEvent.getmDescription());
        return listItem;
    }
}
