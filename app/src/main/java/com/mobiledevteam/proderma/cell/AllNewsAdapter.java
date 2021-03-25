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

public class AllNewsAdapter extends ArrayAdapter<News> {
    private Context mContext;
    private List<News> allNews = new ArrayList<>();

    public AllNewsAdapter(Context context, ArrayList<News> list) {
        super(context, 0 , list);
        mContext = context;
        allNews = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_news,parent,false);

        News currentNews = allNews.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.txt_newstitle);
        title.setText(currentNews.getmTitle());
        TextView description = (TextView) listItem.findViewById(R.id.txt_newsdescription);
        description.setText(currentNews.getmDescription());
        TextView newsDate = (TextView) listItem.findViewById(R.id.txt_newsdate);
        newsDate.setText(currentNews.getmDateTime());
        return listItem;
    }
}
