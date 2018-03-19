package com.vamer.Pharma.pharmacyclientapp.model;

/**
 * Created by Rasoul Miri on 8/23/17.
 */

public class Pharmacy {
    public String getBranchID() {
        return BranchID;
    }

    public String getPharmacyName() {
        return PharmacyName;
    }

    public String getPharmacyDesc() {
        return PharmacyDesc;
    }

    public String getPharmacyLogo() {
        return PharmacyLogo;
    }

    public String getPharmacyRate() {
        return PharmacyRate;
    }

    public String getDeliveryDuration() {
        return DeliveryDuration;
    }

    public String getDistance() {
        return Distance;
    }

    public String getDeliveryZone_KiloMeter() {
        return DeliveryZone_KiloMeter;
    }

    public String getDeliveryWorkinghrsFrom() {
        return DeliveryWorkinghrsFrom;
    }

    public String getDeliveryWorkinghrsTo() {
        return DeliveryWorkinghrsTo;
    }

    public void setBranchID(String branchID) {

        BranchID = branchID;
    }

    public void setPharmacyName(String pharmacyName) {
        PharmacyName = pharmacyName;
    }

    public void setPharmacyDesc(String pharmacyDesc) {
        PharmacyDesc = pharmacyDesc;
    }

    public void setPharmacyLogo(String pharmacyLogo) {

        PharmacyLogo = pharmacyLogo;
    }

    public void setPharmacyRate(String pharmacyRate) {
        PharmacyRate = pharmacyRate;
    }

    public void setDeliveryDuration(String deliveryDuration) {
        DeliveryDuration = deliveryDuration;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public void setDeliveryZone_KiloMeter(String deliveryZone_KiloMeter) {
        DeliveryZone_KiloMeter = deliveryZone_KiloMeter;
    }

    public void setDeliveryWorkinghrsFrom(String deliveryWorkinghrsFrom) {
        DeliveryWorkinghrsFrom = deliveryWorkinghrsFrom;
    }

    public void setDeliveryWorkinghrsTo(String deliveryWorkinghrsTo) {
        DeliveryWorkinghrsTo = deliveryWorkinghrsTo;
    }

    private String BranchID;
    private String PharmacyName;
    private String PharmacyDesc;
    private String PharmacyLogo;
    private String PharmacyRate;
    private String DeliveryDuration;
    private String Distance;
    private String DeliveryZone_KiloMeter;
    private String DeliveryWorkinghrsFrom;
    private String DeliveryWorkinghrsTo;



}
