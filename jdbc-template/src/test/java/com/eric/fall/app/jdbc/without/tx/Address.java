package com.eric.fall.app.jdbc.without.tx;

public class Address {

    public int id;

    public int userId;

    public String address;

    public int zipCode;

    public void setZip(Integer zip) {
        this.zipCode = zip == null ? 0 : zip;
    }






}
