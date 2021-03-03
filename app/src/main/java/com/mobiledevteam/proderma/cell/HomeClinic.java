package com.mobiledevteam.proderma.cell;

import com.google.android.gms.maps.model.LatLng;
import com.mobiledevteam.proderma.Common;

public class HomeClinic {
    private String mId;
    private String mName;
    private String mLocation;
    private String mImage;
    private String mDescription;
    private String mPhone;
    private String mWhatsapp;
    private String mDoctor;
    private LatLng mLatLng;

    public HomeClinic(String id,String name,String location,String image,String description, String phone, String whatsapp, String doctor, LatLng mlatlng){
        mId=id;
        mName=name;
        mLocation = location;
        mImage=image;
        mDescription=description;
        mPhone = phone;
        mWhatsapp = whatsapp;
        mDoctor = doctor;
        mLatLng = mlatlng;
    }

    public String getmId() {
        return mId;
    }

    public String getmLocation() { return mLocation;}

    public String getmName() {
        return mName;
    }

    public String getmImage() {
        return Common.getInstance().getBaseURL()+mImage;
//        return mImage;
    }
    public String getmDescription() {
        return mDescription;
    }

    public String getmDoctor() {return mDoctor;}
    public String getmPhone() {return mPhone;}
    public String getmWhatsapp() {return mWhatsapp;}

    public LatLng getmLatLng() {return mLatLng;}
}
