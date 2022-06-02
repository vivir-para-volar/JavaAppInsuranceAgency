package com.insuranceagency.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class InsuranceEvent {
    /**
     * Поле Id
     */
    private int id;

    /**
     * Поле Дата
     */
    @NotNull
    private LocalDate date;

    /**
     * Поле Страховая выплата
     */
    private int insurancePayment;

    /**
     * Поле Описание
     */
    @NotNull
    private String description;

    /**
     * Поле Id полиса
     */
    private int policyId;

    /**
     * Инициализирует новый экземпляр класса InsuranceEvent с заданными параметрами
     * @param id Id
     * @param date Дата
     * @param insurancePayment Страховая выплата
     * @param description Описание
     * @param policyId Id полиса
     */
    public InsuranceEvent(int id, @NotNull LocalDate date, int insurancePayment, @NotNull String description, int policyId) {
        this.id = id;
        this.date = date;
        this.insurancePayment = insurancePayment;
        this.description = description;
        this.policyId = policyId;
    }

    /**
     * Инициализирует новый экземпляр класса InsuranceEvent с заданными параметрами
     * @param date Дата
     * @param insurancePayment Страховая выплата
     * @param description Описание
     * @param policyId Id полиса
     */
    public InsuranceEvent(@NotNull LocalDate date, int insurancePayment, @NotNull String description, int policyId) {
        this.date = date;
        this.insurancePayment = insurancePayment;
        this.description = description;
        this.policyId = policyId;
    }

    /**
     * Функция получения значение поля {@link InsuranceEvent#id}
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значение поля {@link InsuranceEvent#date}
     * @return Дата
     */
    public @NotNull LocalDate getDate() {
        return date;
    }

    /**
     * Функция получения значение поля {@link InsuranceEvent#insurancePayment}
     * @return Страховая выплата
     */
    public int getInsurancePayment() {
        return insurancePayment;
    }

    /**
     * Функция получения значение поля {@link InsuranceEvent#description}
     * @return Описание
     */
    public @NotNull String getDescription() {
        return description;
    }

    /**
     * Функция получения значение поля {@link InsuranceEvent#policyId}
     * @return Id полиса
     */
    public int getPolicyId() {
        return policyId;
    }
}
