package com.mobiledevteam.proderma.cell;

import com.mobiledevteam.proderma.Common;

public class ImageSliderPhoto {
    private String mID;
    private String mStatus;
    private String mUrl;

    public ImageSliderPhoto(String id, String status, String url){
        mID = id;
        mStatus=status;
        mUrl=url;
    }

    public String getmID() {return mID;}
    public String getmStatus() {
        return mStatus;
    }
    public String getmUrl() {
        return Common.getInstance().getBaseURL() +mUrl;
    }
}
