package com.insuranceagency.model;

import org.jetbrains.annotations.NotNull;

public class PersonAllowedToDrive {
    /**
     * Поле Id
     */
    private int id;

    /**
     * Поле ФИО
     */
    @NotNull
    private String fullName;

    /**
     * Поле Водительское удостоверение
     */
    @NotNull
    private String drivingLicence;

    /**
     * Инициализирует новый экземпляр класса PersonAllowedToDrive с заданными параметрами
     * @param id Id
     * @param fullName ФИО
     * @param drivingLicence Водительское удостоверение
     */
    public PersonAllowedToDrive(int id, @NotNull String fullName, @NotNull String drivingLicence) {
        this.id = id;
        this.fullName = fullName;
        this.drivingLicence = drivingLicence;
    }

    /**
     * Инициализирует новый экземпляр класса PersonAllowedToDrive с заданными параметрами
     * @param fullName ФИО
     * @param drivingLicence Водительское удостоверение
     */
    public PersonAllowedToDrive(@NotNull String fullName, @NotNull String drivingLicence) {
        this.fullName = fullName;
        this.drivingLicence = drivingLicence;
    }

    /**
     * Функция получения значение поля {@link PersonAllowedToDrive#id}
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значение поля {@link PersonAllowedToDrive#fullName}
     * @return ФИО
     */
    public @NotNull String getFullName() {
        return fullName;
    }

    /**
     * Функция получения значение поля {@link PersonAllowedToDrive#fullName}
     * @return Водительское удостоверение
     */
    public @NotNull String getDrivingLicence() {
        return drivingLicence;
    }
}