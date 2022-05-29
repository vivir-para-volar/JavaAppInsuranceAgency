package com.insuranceagency.model;

import java.time.LocalDate;

public class InsuranceEvent {
    private int id;
    private LocalDate date;
    private int insurancePayment;
    private String description;
    private int policyId;

    public InsuranceEvent(int id, LocalDate date, int insurancePayment, String description, int policyId) {
        this.id = id;
        this.date = date;
        this.insurancePayment = insurancePayment;
        this.description = description;
        this.policyId = policyId;
    }

    public InsuranceEvent(LocalDate date, int insurancePayment, String description, int policyId) {
        this.date = date;
        this.insurancePayment = insurancePayment;
        this.description = description;
        this.policyId = policyId;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getInsurancePayment() {
        return insurancePayment;
    }

    public String getDescription() {
        return description;
    }

    public int getPolicyId() {
        return policyId;
    }
}
