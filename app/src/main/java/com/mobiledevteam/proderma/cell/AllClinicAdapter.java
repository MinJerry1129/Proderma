package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.util.Log;
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

public class AllClinicAdapter extends ArrayAdapter<HomeClinic> {
    private Context mContext;
    private List<HomeClinic> allClinicList = new ArrayList<>();

    public AllClinicAdapter(Context context, ArrayList<HomeClinic> list) {
        super(context, 0 , list);
        mContext = context;
        allClinicList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_all_clinic,parent,false);

        HomeClinic currentClinic = allClinicList.get(position);
//
        ImageView clinic_image = (ImageView) listItem.findViewById(R.id.clinicimage);
        Ion.with(mContext).load(currentClinic.getmImage()).intoImageView(clinic_image);//
        TextView name = (TextView) listItem.findViewById(R.id.txt_clinicname);
        name.setText(currentClinic.getmName());
        TextView location = (TextView) listItem.findViewById(R.id.txt_location);
        location.setText(currentClinic.getmLocation());
        TextView txtDoctor = (TextView) listItem.findViewById(R.id.txt_doctor);
        txtDoctor.setText(currentClinic.getmDoctor());
        TextView txtPhone = (TextView) listItem.findViewById(R.id.txt_phone);
        txtPhone.setText(currentClinic.getmPhone());
        return listItem;
    }
}