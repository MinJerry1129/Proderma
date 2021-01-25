package com.mobiledevteam.proderma.cell;

public class ClinicDoctor {
    private String mId;
    private String mName;
    private String mAge;
    private String mInfo;
    private String mImage;

    public ClinicDoctor(String id,String name,String age,String info, String image){
        mId=id;
        mName=name;
        mAge = age;
        mInfo = info;
        mImage = image;
    }

    public String getmId() {
        return mId;
    }
    public String getmName() {
        return mName;
    }
    public String getmAge() { return mAge;}
    public String getmInfo() { return mInfo;}
    public String getmImage() {return mImage;}
}

