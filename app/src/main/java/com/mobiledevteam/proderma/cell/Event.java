package com.mobiledevteam.proderma.cell;

import com.mobiledevteam.proderma.Common;

public class Event {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mImage;

    public Event(String id,String title,String description,String image){
        mId=id;
        mTitle=title;
        mDescription=description;
        mImage=image;

    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {return mTitle;}

    public String getmImage() {
        return Common.getInstance().getBaseURL()+mImage;
//        return mImage;
    }
    public String getmDescription() {
        return mDescription;
    }

}
