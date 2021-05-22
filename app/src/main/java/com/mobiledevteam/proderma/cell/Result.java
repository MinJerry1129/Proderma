package com.mobiledevteam.proderma.cell;

public class Result {
    private String mId;
    private String mImageurl;

    public Result(String id,String imageurl){
        mId=id;
        mImageurl = imageurl;
    }
    public String getmId() {
        return mId;
    }
    public String getmImageurl() { return mImageurl; }
}