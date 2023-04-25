package com.trader.joes.model;

public class Store {
    private String _distance;
    private String _distanceuom;
    private String clientkey;
    private String name;
    private String address1;
    private String city;
    private String state;
    private String postalcode;
    private String country;
    private String phone;
    private double latitude;
    private double longitude;
    private String hours;
    private String locationimage;

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public String get_distanceuom() {
        return _distanceuom;
    }

    public void set_distanceuom(String _distanceuom) {
        this._distanceuom = _distanceuom;
    }

    public String getClientkey() {
        return clientkey;
    }

    public void setClientkey(String clientkey) {
        this.clientkey = clientkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getLocationimage() {
        return locationimage;
    }

    public void setLocationimage(String locationimage) {
        this.locationimage = locationimage;
    }
}
