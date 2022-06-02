package com.insuranceagency.model;

import org.jetbrains.annotations.NotNull;

public class Car {
    /**
     * Поле Id
     */
    private int id;

    /**
     * Поле Модель
     */
    @NotNull
    private String model;

    /**
     * Поле VIN номер
     */
    @NotNull
    private String vin;

    /**
     * Поле Регистрационный знак
     */
    @NotNull
    private String registrationPlate;

    /**
     * Поле Паспорт ТС
     */
    @NotNull
    private String vehiclePassport;

    /**
     * Инициализирует новый экземпляр класса Car с заданными параметрами
     * @param id Id
     * @param model Модель
     * @param vin VIN номер
     * @param registrationPlate Регистрационный знак
     * @param vehiclePassport Паспорт ТС
     */
    public Car(int id, @NotNull String model, @NotNull String vin, @NotNull String registrationPlate, @NotNull String vehiclePassport) {
        this.id = id;
        this.model = model;
        this.vin = vin;
        this.registrationPlate = registrationPlate;
        this.vehiclePassport = vehiclePassport;
    }

    /**
     * Инициализирует новый экземпляр класса Car с заданными параметрами
     * @param model Модель
     * @param vin VIN номер
     * @param registrationPlate Регистрационный знак
     * @param vehiclePassport Паспорт ТС
     */
    public Car(@NotNull String model, @NotNull String vin, @NotNull String registrationPlate, @NotNull String vehiclePassport) {
        this.model = model;
        this.vin = vin;
        this.registrationPlate = registrationPlate;
        this.vehiclePassport = vehiclePassport;
    }

    /**
     * Функция получения значение поля {@link Car#id}
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значение поля {@link Car#model}
     * @return Модель
     */
    public @NotNull String getModel() {
        return model;
    }

    /**
     * Функция получения значение поля {@link Car#vin}
     * @return VIN номер
     */
    public @NotNull String getVin() {
        return vin;
    }

    /**
     * Функция получения значение поля {@link Car#registrationPlate}
     * @return Регистрационный знак
     */
    public @NotNull String getRegistrationPlate() {
        return registrationPlate;
    }

    /**
     * Функция получения значение поля {@link Car#vehiclePassport}
     * @return Паспорт ТС
     */
    public @NotNull String getVehiclePassport() {
        return vehiclePassport;
    }
}
