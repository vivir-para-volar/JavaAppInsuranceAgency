package com.insuranceagency.model;

public class Connection {
    private int policyId;
    private int personAllowedToDriveId;

    public Connection(int policyId, int personAllowedToDriveId) {
        this.policyId = policyId;
        this.personAllowedToDriveId = personAllowedToDriveId;
    }

    public int getPolicyId() {
        return policyId;
    }

    public int getPersonAllowedToDriveId() {
        return personAllowedToDriveId;
    }
}
