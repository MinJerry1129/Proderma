package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobiledevteam.proderma.R;

import java.util.ArrayList;
import java.util.List;

public class AllPdfAdapter extends ArrayAdapter<AllPdf> {
    private Context mContext;
    private List<AllPdf> allPdfList = new ArrayList<>();

    public AllPdfAdapter(Context context, ArrayList<AllPdf> list) {
        super(context, 0 , list);
        mContext = context;
        allPdfList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_all_pdf,parent,false);

        AllPdf currentVideo = allPdfList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.txt_pdftitle);
        title.setText(currentVideo.getmTitle());
        TextView description = (TextView) listItem.findViewById(R.id.txt_description);
        description.setText(currentVideo.getmDescription());
        return listItem;
    }
}
