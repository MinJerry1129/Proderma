package com.mobiledevteam.proderma.cell;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        Button infourl = (Button)listItem.findViewById(R.id.btn_moreinfo);
        infourl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentNews.getmInfourl().equals("")){
                    Toast.makeText(mContext,"Not more information",Toast.LENGTH_LONG).show();
                }else{
                    Uri uri = Uri.parse(currentNews.getmInfourl()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

            }
        });
        return listItem;
    }
}
