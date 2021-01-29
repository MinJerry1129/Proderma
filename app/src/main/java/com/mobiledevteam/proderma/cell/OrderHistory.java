package com.mobiledevteam.proderma.cell;

import com.mobiledevteam.proderma.Common;

public class OrderHistory {
    private String mId;
    private String mProductName;
    private String mProductImage;
    private String mCount;
    private String mExtra;
    private String mDate;
    private String mStatus;

    public OrderHistory(String id,String productname,String productimage,String count,String extra, String orderdate,String status){
        mId=id;
        mProductName=productname;
        mProductImage = productimage;
        mCount=count;
        mExtra=extra;
        mDate = orderdate;
        mStatus = status;
    }

    public String getmId() {
        return mId;
    }
    public String getmProductName() { return mProductName;}
    public String getmCount() {return mCount;}
    public String getmExtra() { return mExtra;}
    public String getmDate() { return mDate;}
    public String getmStatus() {return mStatus;}

    public String getmProductImage() {
        return Common.getInstance().getBaseURL()+mProductImage;
    }
}
