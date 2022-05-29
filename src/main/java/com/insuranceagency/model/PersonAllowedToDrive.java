package com.insuranceagency.model;

public class PersonAllowedToDrive {
    private int id;
    private String fullName;
    private String drivingLicence;

    public PersonAllowedToDrive(int id, String fullName, String drivingLicence) {
        this.id = id;
        this.fullName = fullName;
        this.drivingLicence = drivingLicence;
    }

    public PersonAllowedToDrive(String fullName, String drivingLicence) {
        this.fullName = fullName;
        this.drivingLicence = drivingLicence;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }
}
