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

public class ClinicOfferAdapter extends ArrayAdapter<ClinicOffer> {
    private Context mContext;
    private List<ClinicOffer> allClinicOfferList = new ArrayList<>();

    public ClinicOfferAdapter(Context context, ArrayList<ClinicOffer> list) {
        super(context, 0 , list);
        mContext = context;
        allClinicOfferList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_clinic_offer,parent,false);

        ClinicOffer currentClinicOffer = allClinicOfferList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.txt_offertitle);
        title.setText(currentClinicOffer.getmTitle());
        TextView description = (TextView) listItem.findViewById(R.id.txt_offerdescription);
        description.setText(currentClinicOffer.getmInfo());
        return listItem;
    }
}