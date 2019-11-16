package com.shadowraylabs.chokoartist.Model;

import java.io.Serializable;

public class Users implements Serializable {
    private String name;
    private String phoneNo;
    private String password;
    private Integer noOfReferrals;
    private Integer storeCredits;
    private Boolean isAdmin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getNoOfReferrals() {
        return noOfReferrals;
    }

    public void setNoOfReferrals(Integer noOfReferrals) {
        this.noOfReferrals = noOfReferrals;
    }

    public Integer getStoreCredits() {
        return storeCredits;
    }

    public void setStoreCredits(Integer storeCredits) {
        this.storeCredits = storeCredits;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
