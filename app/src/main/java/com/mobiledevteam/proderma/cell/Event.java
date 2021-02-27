package com.mobiledevteam.proderma.cell;

import com.mobiledevteam.proderma.Common;

public class Event {
    private String mId;
    private String mTitle;
    private String mDateTime;
    private String mLocation;
    private String mDescription;
    private String mImage;

    public Event(String id,String title,String dateTime,String elocation,String description,String image){
        mId=id;
        mTitle=title;
        mDateTime = dateTime;
        mLocation = elocation;
        mDescription=description;
        mImage=image;

    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {return mTitle;}

    public String getmDateTime() {return mDateTime;}

    public String getmLocation() {return mLocation;}

    public String getmImage() {
        return Common.getInstance().getBaseURL()+mImage;
//        return mImage;
    }
    public String getmDescription() {
        return mDescription;
    }

}
