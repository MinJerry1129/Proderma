package com.mobiledevteam.proderma.cell;

import com.mobiledevteam.proderma.Common;

public class HomeProduct {
    private String mId;
    private String mName;
    private String mPrice;
    private String mImage;
    private String mDescription;

    public HomeProduct(String id,String name,String price,String image,String description){
        mId=id;
        mName=name;
        mPrice = price;
        String currentString = image;
        String[] separated = currentString.split("_split_");
        mImage=separated[0];
        mDescription=description;
    }

    public String getmId() {
        return mId;
    }

    public String getmPrice() { return mPrice;}

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
}
