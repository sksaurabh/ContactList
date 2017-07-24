package com.contactlist.contactlist.bean;

/**
 * Created by saurabh on 5/20/2017.
 */

public class ContactDetails {
    private String mFName;
    private String mLName;
    private String pNo;
    private String nName;
    private String id;

    public ContactDetails(String mFName, String mLName, String pNo, String nName) {
        this.mFName = mFName;
        this.mLName = mLName;
        this.pNo = pNo;
        this.nName = nName;
    }

    public ContactDetails() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmFName() {
        return mFName;
    }

    public void setmFName(String mFName) {
        this.mFName = mFName;
    }

    public String getmLName() {
        return mLName;
    }

    public void setmLName(String mLName) {
        this.mLName = mLName;
    }

    public String getpNo() {
        return pNo;
    }

    public void setpNo(String pNo) {
        this.pNo = pNo;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

}
