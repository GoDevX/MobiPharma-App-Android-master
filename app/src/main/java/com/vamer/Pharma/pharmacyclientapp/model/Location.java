package com.vamer.Pharma.pharmacyclientapp.model;

/**
 * Created by Ahmed.Khames on 2/27/2018.
 */

public class Location {
    private String locationName;

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationID() {
        return locationID;
    }

    private String locationID;
    public Location(String locationID,String locationName, String locationAddress, String locationBuildingNumber, String locationFloorNumber, String locationAppartmentNumber, String GPS_Latitude, String GPS_Longitude) {
        this.locationName = locationName;
        this.locationID=locationID;
        this.locationAddress = locationAddress;
        this.locationBuildingNumber = locationBuildingNumber;
        this.locationFloorNumber = locationFloorNumber;
        this.locationAppartmentNumber = locationAppartmentNumber;
        this.GPS_Latitude = GPS_Latitude;
        this.GPS_Longitude = GPS_Longitude;
    }
    public Location(String locationName, String locationAddress, String locationBuildingNumber, String locationFloorNumber, String locationAppartmentNumber, String GPS_Latitude, String GPS_Longitude) {
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationBuildingNumber = locationBuildingNumber;
        this.locationFloorNumber = locationFloorNumber;
        this.locationAppartmentNumber = locationAppartmentNumber;
        this.GPS_Latitude = GPS_Latitude;
        this.GPS_Longitude = GPS_Longitude;
    }
    public String getLocationName() {
        return locationName;

    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationBuildingNumber() {
        return locationBuildingNumber;
    }

    public String getLocationFloorNumber() {
        return locationFloorNumber;
    }

    public String getLocationAppartmentNumber() {
        return locationAppartmentNumber;
    }

    public String getGPS_Latitude() {
        return GPS_Latitude;
    }

    public String getGPS_Longitude() {
        return GPS_Longitude;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public void setLocationBuildingNumber(String locationBuildingNumber) {
        this.locationBuildingNumber = locationBuildingNumber;
    }

    public void setLocationFloorNumber(String locationFloorNumber) {
        this.locationFloorNumber = locationFloorNumber;
    }

    public void setLocationAppartmentNumber(String locationAppartmentNumber) {
        this.locationAppartmentNumber = locationAppartmentNumber;
    }

    public void setGPS_Latitude(String GPS_Latitude) {
        this.GPS_Latitude = GPS_Latitude;
    }

    public void setGPS_Longitude(String GPS_Longitude) {
        this.GPS_Longitude = GPS_Longitude;
    }

    private String locationAddress;
    private String locationBuildingNumber;
    private String locationFloorNumber;
    private String locationAppartmentNumber;
    private String GPS_Latitude;
    private String GPS_Longitude;

}
