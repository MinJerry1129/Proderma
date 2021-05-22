package com.mobiledevteam.proderma;

public class Common {
    private static Common instance = new Common();
    private String baseURL = "http://phpstack-362651-1717329.cloudwaysapps.com/";//http://phpstack-362651-1717329.cloudwaysapps.com/ , http://10.0.2.2/proderma/
//    private String baseURL = "http://proderma.online/";
//    private String baseURL = "http://10.0.2.2/proderma/";
    private String clinicpagetype;
    private String login_status="no";
    private String clinicID;
    private String selLang = "en";
    private String clinictype = "normal";
    private String imgUrl = "";

    public void Comon(){
        //this.baseURL="http://localhost/jsontest/";
    }

    public static Common getInstance() {
        return instance;
    }
    public String getBaseURL() {return baseURL;}
    public String getClinicpagetype() { return clinicpagetype;}
    public void setClinicpagetype(String clinicpagetype) {this.clinicpagetype = clinicpagetype;}

    public String getLogin_status() {return login_status;}
    public void setLogin_status(String login_status) {this.login_status = login_status;}

    public String getClinicID() {return clinicID;}
    public void setClinicID(String clinicID) {this.clinicID = clinicID;}

    public String getSelLang() {return selLang;}
    public void setSelLang(String selLang) {this.selLang = selLang;}

    public String getClinictype() { return clinictype;}
    public void setClinictype(String clinictype) {this.clinictype = clinictype;}

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}