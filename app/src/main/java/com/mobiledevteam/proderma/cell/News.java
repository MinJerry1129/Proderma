package com.mobiledevteam.proderma.cell;

import com.mobiledevteam.proderma.Common;

public class News {
    private String mId;
    private String mTitle;
    private String mDateTime;
    private String mDescription;
    private String mInfourl;
    private String mType;

    public News(String id,String title,String dateTime,String description,String infourl,String type){
        mId=id;
        mTitle=title;
        mDateTime = dateTime;
        mDescription=description;
        mInfourl=infourl;
        mType=type;

    }

    public String getmId() {
        return mId;
    }
    public String getmTitle() {return mTitle;}
    public String getmDateTime() {return mDateTime;}
    public String getmDescription() {
        return mDescription;
    }
    public String getmInfourl() { return mInfourl; }
    public String getmType() { return mType;}
}
