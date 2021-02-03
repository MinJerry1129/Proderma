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

public class AllImageAdapter extends ArrayAdapter<ImageSliderPhoto> {
    private Context mContext;
    private List<ImageSliderPhoto> allImageList = new ArrayList<>();

    public AllImageAdapter(Context context, ArrayList<ImageSliderPhoto> list) {
        super(context, 0 , list);
        mContext = context;
        allImageList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_all_clinic_image,parent,false);

        ImageSliderPhoto currentImage = allImageList.get(position);
//
        ImageView clinic_image = (ImageView) listItem.findViewById(R.id.img_clinic);
        Ion.with(mContext).load(currentImage.getmUrl()).intoImageView(clinic_image);
        TextView status = (TextView) listItem.findViewById(R.id.txt_imagestatus);
        status.setText(currentImage.getmStatus());
//        TextView description = (TextView) listItem.findViewById(R.id.txt_description);
//        description.setText(currentEvent.getmDescription());
        return listItem;
    }
}