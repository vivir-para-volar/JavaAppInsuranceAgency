package com.insuranceagency.model;

import java.util.Date;

public class Photo {
    private int id;
    private String path;
    private Date uploadDate;
    private int carId;

    public Photo(int id, String path, Date uploadDate, int carId) {
        this.id = id;
        this.path = path;
        this.uploadDate = uploadDate;
        this.carId = carId;
    }

    public Photo(String path, Date uploadDate, int carId) {
        this.path = path;
        this.uploadDate = uploadDate;
        this.carId = carId;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public int getCarId() {
        return carId;
    }
}
