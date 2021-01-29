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

public class AllVideoAdapter extends ArrayAdapter<AllVideo> {
    private Context mContext;
    private List<AllVideo> allVideoList = new ArrayList<>();

    public AllVideoAdapter(Context context, ArrayList<AllVideo> list) {
        super(context, 0 , list);
        mContext = context;
        allVideoList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_all_video,parent,false);

        AllVideo currentVideo = allVideoList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.txt_videotitle);
        title.setText(currentVideo.getmTitle());
        TextView description = (TextView) listItem.findViewById(R.id.txt_description);
        description.setText(currentVideo.getmDescription());
        return listItem;
    }
}