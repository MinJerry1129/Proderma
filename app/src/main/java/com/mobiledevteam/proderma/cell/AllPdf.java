package com.mobiledevteam.proderma.cell;

import com.mobiledevteam.proderma.Common;

public class AllPdf {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mUrl;

    public AllPdf(String id,String title,String description,String url){
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
        return Common.getInstance().getBaseURL() +mUrl;
    }
    public String getmDescription() {
        return mDescription;
    }

}
