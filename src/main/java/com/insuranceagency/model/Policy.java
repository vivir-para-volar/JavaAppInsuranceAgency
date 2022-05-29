package com.insuranceagency.model;

import java.util.Date;

public class Policy {
    private int id;
    private String insuranceType;
    private int insurancePremium;
    private int insuranceAmount;
    private Date dateOfConclusion;
    private Date expirationDate;
    private int policyholderId;
    private int carId;
    private int employeeId;

    public Policy(int id, String insuranceType, int insurancePremium, int insuranceAmount, Date dateOfConclusion, Date expirationDate, int policyholderId, int carId, int employeeId) {
        this.id = id;
        this.insuranceType = insuranceType;
        this.insurancePremium = insurancePremium;
        this.insuranceAmount = insuranceAmount;
        this.dateOfConclusion = dateOfConclusion;
        this.expirationDate = expirationDate;
        this.policyholderId = policyholderId;
        this.carId = carId;
        this.employeeId = employeeId;
    }

    public Policy(String insuranceType, int insurancePremium, int insuranceAmount, Date dateOfConclusion, Date expirationDate, int policyholderId, int carId, int employeeId) {
        this.insuranceType = insuranceType;
        this.insurancePremium = insurancePremium;
        this.insuranceAmount = insuranceAmount;
        this.dateOfConclusion = dateOfConclusion;
        this.expirationDate = expirationDate;
        this.policyholderId = policyholderId;
        this.carId = carId;
        this.employeeId = employeeId;
    }

    public int getId() {
        return id;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public int getInsurancePremium() {
        return insurancePremium;
    }

    public int getInsuranceAmount() {
        return insuranceAmount;
    }

    public Date getDateOfConclusion() {
        return dateOfConclusion;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public int getPolicyholderId() {
        return policyholderId;
    }

    public int getCarId() {
        return carId;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
