package com.vamer.Pharma.pharmacyclientapp.model;

/**
 * Created by Ahmed.Khames on 2/25/2018.
 */

public class Customer {

    public Customer(String date, String name, String token, String gender,String phone) {
      this.date = date;
        Name = name;
        Token = token;
        Gender = gender;
        this.phone=phone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getdate() {
        return date;
    }

    public String getName() {
        return Name;
    }

    public String getToken() {
        return Token;
    }

    public String getGender() {
        return Gender;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    String date;
    String Name;
    String Token;
    String Gender;




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;

    }

    String  phone;

}
