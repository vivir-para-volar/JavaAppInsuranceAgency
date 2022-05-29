package com.insuranceagency.model;

public class Car {
    private int id;
    private String model;
    private String vin;
    private String registrationPlate;
    private String vehiclePassport;

    public Car(int id, String model, String vin, String registrationPlate, String vehiclePassport) {
        this.id = id;
        this.model = model;
        this.vin = vin;
        this.registrationPlate = registrationPlate;
        this.vehiclePassport = vehiclePassport;
    }

    public Car(String model, String vin, String registrationPlate, String vehiclePassport) {
        this.model = model;
        this.vin = vin;
        this.registrationPlate = registrationPlate;
        this.vehiclePassport = vehiclePassport;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getVin() {
        return vin;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public String getVehiclePassport() {
        return vehiclePassport;
    }
}
