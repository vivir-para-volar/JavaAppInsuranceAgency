package com.insuranceagency.model;

import java.time.LocalDate;
import org.jetbrains.annotations.NotNull;

/**
 * Класс Страхователь со свойствами <b>id</b>, <b>fullName</b>, <b>birthday</b> и <b>telephone</b>.
 * <p>Данный класс позволяет описать экземпляр страхователя с заданным параметрами.</p>
 */
public class Policyholder {
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
     * Поле Дата рождения
     */
    @NotNull
    private LocalDate birthday;

    /**
     * Поле Номер телефона
     */
    @NotNull
    private String telephone;

    /**
     * Поле Паспорт
     */
    @NotNull
    private String passport;

    /**
     * Инициализирует новый экземпляр класса Policyholder с заданными параметрами
     * @param id Id
     * @param fullName ФИО
     * @param birthday Дата рождения
     * @param telephone Номер телефона
     * @param passport Пасспорт
     */
    public Policyholder(int id, @NotNull String fullName, @NotNull LocalDate birthday, @NotNull String telephone, @NotNull String passport) {
        this.id = id;
        this.fullName = fullName;
        this.birthday = birthday;
        this.telephone = telephone;
        this.passport = passport;
    }

    /**
     * Инициализирует новый экземпляр класса Policyholder с заданными параметрами
     * @param fullName ФИО
     * @param birthday Дата рождения
     * @param telephone Номер телефона
     * @param passport Пасспорт
     */
    public Policyholder(@NotNull String fullName, @NotNull LocalDate birthday, @NotNull String telephone, @NotNull String passport) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.telephone = telephone;
        this.passport = passport;
    }

    /**
     * Функция получения значение поля {@link Policyholder#id}
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значение поля {@link Policyholder#fullName}
     * @return ФИО
     */
    public @NotNull String getFullName() {
        return fullName;
    }

    /**
     * Функция получения значение поля {@link Policyholder#birthday}
     * @return Дата рождения
     */
    public @NotNull LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Функция получения значение поля {@link Policyholder#telephone}
     * @return Номер телефона
     */
    public @NotNull String getTelephone() {
        return telephone;
    }

    /**
     * Функция получения значение поля {@link Policyholder#passport}
     * @return Паспорт
     */
    public @NotNull String getPassport() {
        return passport;
    }
}