package com.insuranceagency.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class Employee {
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
     * Поле Логин
     */
    @NotNull
    private String login;

    /**
     * Поле Пароль
     */
    @NotNull
    private String password;

    /**
     * Инициализирует новый экземпляр класса Employee с заданными параметрами
     * @param id Id
     * @param fullName ФИО
     * @param birthday Дата рождения
     * @param telephone Номер телефона
     * @param passport Пасспорт
     * @param login Логин
     * @param password Пароль
     */
    public Employee(int id, @NotNull String fullName, @NotNull LocalDate birthday, @NotNull String telephone, @NotNull String passport, @NotNull String login, @NotNull String password) {
        this.id = id;
        this.fullName = fullName;
        this.birthday = birthday;
        this.telephone = telephone;
        this.passport = passport;
        this.login = login;
        this.password = password;
    }

    /**
     * Инициализирует новый экземпляр класса Employee с заданными параметрами
     * @param fullName ФИО
     * @param birthday Дата рождения
     * @param telephone Номер телефона
     * @param passport Пасспорт
     * @param login Логин
     * @param password Пароль
     */
    public Employee(@NotNull String fullName, @NotNull LocalDate birthday, @NotNull String telephone, @NotNull String passport, @NotNull String login, @NotNull String password) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.telephone = telephone;
        this.passport = passport;
        this.login = login;
        this.password = password;
    }

    /**
     * Функция получения значение поля {@link Employee#id}
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значение поля {@link Employee#fullName}
     * @return ФИО
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Функция получения значение поля {@link Employee#birthday}
     * @return Дата рождения
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Функция получения значение поля {@link Employee#telephone}
     * @return Номер телефона
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Функция получения значение поля {@link Employee#passport}
     * @return Паспорт
     */
    public String getPassport() {
        return passport;
    }

    /**
     * Функция получения значение поля {@link Employee#login}
     * @return Логин
     */
    public String getLogin() {
        return login;
    }

    /**
     * Функция получения значение поля {@link Employee#password}
     * @return Пароль
     */
    public String getPassword() {
        return password;
    }
}
