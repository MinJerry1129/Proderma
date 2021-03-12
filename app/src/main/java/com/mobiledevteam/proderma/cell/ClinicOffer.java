package com.mobiledevteam.proderma.cell;

public class ClinicOffer {
    private String mId;
    private String mTitle;
    private String mInfo;
    private String mStatus;

    public ClinicOffer(String id,String title,String info, String status){
        mId=id;
        mTitle=title;
        mInfo = info;
        mStatus = status;
    }

    public String getmId() {
        return mId;
    }
    public String getmTitle() { return mTitle;}
    public String getmInfo() { return mInfo;}
    public String getmStatus() {return mStatus;}
}
