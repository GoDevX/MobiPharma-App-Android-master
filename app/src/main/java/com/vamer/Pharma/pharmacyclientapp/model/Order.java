package com.vamer.Pharma.pharmacyclientapp.model;

/**
 * Created by arka on 4/30/16.
 */

import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;

/**
 * Schema
 * {
 * "uid": "34343434",
 * "address": "dfererer",
 * "prescription_url": "dfdfdfdf",
 * "price": 3434.3434,
 * "shipping_charge": 334.3434,
 * "created_at": 343434
 * "is_completed": true,
 * "is_canceled": true,
 * "note": "",
 * "status": "PENDING" OR "CLOSED"
 * }
 */


public class Order {

    public String getOrderID() {
        return OrderID;
    }

    public String getDateTimeStamp() {
        return DateTimeStamp;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public String getStatusID() {
        return StatusID;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getPharmacyID() {
        return PharmacyID;
    }

    public String getPharmacyName() {
        return PharmacyName;
    }

    public String getPharmacyLogoName() {
        return PharmacyLogoName;
    }

    public String getBranchID() {
        return BranchID;
    }

    public String getBranchName() {
        return BranchName;
    }

    private String OrderID;

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        DateTimeStamp = dateTimeStamp;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setPharmacyID(String pharmacyID) {
        PharmacyID = pharmacyID;
    }

    public void setPharmacyName(String pharmacyName) {
        PharmacyName = pharmacyName;
    }

    public void setPharmacyLogoName(String pharmacyLogoName) {

        PharmacyLogoName = PharmacyLogoURL+pharmacyLogoName;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
    private String  PharmacyLogoURL;
    private String DateTimeStamp;
    private String TotalPrice;
    private String StatusID;
    private String OrderStatus;
    private String PharmacyID;
    private String PharmacyName;
    private String PharmacyLogoName;
    private String BranchID;
    private String BranchName;


    public String getPharmacyLogoURL() {
        return PharmacyLogoURL;
    }

    public void setPharmacyLogoURL(String pharmacyLogoURL) {
        PharmacyLogoURL = pharmacyLogoURL;
    }
}