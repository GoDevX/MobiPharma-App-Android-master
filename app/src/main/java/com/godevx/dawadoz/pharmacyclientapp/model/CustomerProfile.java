package com.godevx.dawadoz.pharmacyclientapp.model;

/**
 * Created by Ahmed.Khames on 3/26/2018.
 */

public class CustomerProfile {

    public void setCustName(String custName) {
        CustName = custName;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getCustName() {
        return CustName;
    }

    public String getGender() {
        return Gender;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    String CustName,Gender,BirthDate;
}
