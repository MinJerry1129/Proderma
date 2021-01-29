package com.mobiledevteam.proderma.cell;

public class AllVideo {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mUrl;

    public AllVideo(String id,String title,String description,String url){
        mId=id;
        mTitle=title;
        mDescription=description;
        mUrl=url;

    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {return mTitle;}

    public String getmUrl() {
        return mUrl;
    }
    public String getmDescription() {
        return mDescription;
    }
}
