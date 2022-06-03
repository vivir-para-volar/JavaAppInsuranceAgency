package com.insuranceagency.model;

import com.insuranceagency.database.DBCar;
import com.insuranceagency.database.DBEmployee;
import com.insuranceagency.database.DBPolicyholder;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;

/**
 * Класс Полис со свойствами <b>id</b>, <b>insuranceType</b>, <b>insurancePremium</b>, <b>insuranceAmount</b>,
 * <b>dateOfConclusion</b>, <b>expirationDate</b>, <b>policyholderId</b>, <b>carId</b> и <b>employeeId</b>.
 * <p>Данный класс позволяет описать экземпляр полиса с заданным параметрами.</p>
 */
public class Policy {
    /**
     * Поле Id
     */
    private int id;

    /**
     * Поле Вид страхования
     */
    @NotNull
    private String insuranceType;

    /**
     * Поле Страховая премия
     */
    private int insurancePremium;
    /**
     * Поле Страховая сумма
     */
    private int insuranceAmount;

    /**
     * Поле Дата заключения
     */
    @NotNull
    private LocalDate dateOfConclusion;
    /**
     * Поле Дата окончания действия
     */
    @NotNull
    private LocalDate expirationDate;

    /**
     * Поле Id страхователя
     */
    private int policyholderId;
    /**
     * Поле Id автомобиля
     */
    private int carId;
    /**
     * Поле Id сотрудника
     */
    private int employeeId;

    /**
     * Поле Имя страхователя
     */
    private String policyholderName;
    /**
     * Поле Модель автомобиля
     */
    private String carModel;
    /**
     * Поле Имя сотрудника
     */
    private String employeeName;

    /**
     * Инициализирует новый экземпляр класса Policy с заданными параметрами
     * @param id Id
     * @param insuranceType Вид страхования
     * @param insurancePremium Страховая премия
     * @param insuranceAmount Страховая сумма
     * @param dateOfConclusion Дата заключения
     * @param expirationDate Дата окончания действия
     * @param policyholderId Id страхователя
     * @param carId Id автомобиля
     * @param employeeId Id сотрудника
     */
    public Policy(int id, @NotNull String insuranceType, int insurancePremium, int insuranceAmount, @NotNull LocalDate dateOfConclusion, @NotNull LocalDate expirationDate, int policyholderId, int carId, int employeeId) {
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

    /**
     * Инициализирует новый экземпляр класса Policy с заданными параметрами
     * @param insuranceType Вид страхования
     * @param insurancePremium Страховая премия
     * @param insuranceAmount Страховая сумма
     * @param dateOfConclusion Дата заключения
     * @param expirationDate Дата окончания действия
     * @param policyholderId Id страхователя
     * @param carId Id автомобиля
     * @param employeeId Id сотрудника
     */
    public Policy(@NotNull String insuranceType, int insurancePremium, int insuranceAmount, @NotNull LocalDate dateOfConclusion, @NotNull LocalDate expirationDate, int policyholderId, int carId, int employeeId) {
        this.insuranceType = insuranceType;
        this.insurancePremium = insurancePremium;
        this.insuranceAmount = insuranceAmount;
        this.dateOfConclusion = dateOfConclusion;
        this.expirationDate = expirationDate;
        this.policyholderId = policyholderId;
        this.carId = carId;
        this.employeeId = employeeId;
    }

    /**
     * Инициализирует поля {@link Policy#policyholderName}, {@link Policy#carModel}, {@link Policy#employeeName}
     */
    public void searchName() throws Exception{
        Policyholder policyholder = DBPolicyholder.searchPolicyholderID(policyholderId);
        policyholderName = policyholder.getFullName();

        Car car = DBCar.searchCarID(carId);
        carModel = car.getModel();

        Employee employee = DBEmployee.searchEmployeeID(employeeId);
        employeeName = employee.getFullName();
    }

    /**
     * Функция получения значение поля {@link Policy#id}
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значение поля {@link Policy#insuranceType}
     * @return Вид страхования
     */
    public @NotNull String getInsuranceType() {
        return insuranceType;
    }

    /**
     * Функция получения значение поля {@link Policy#insurancePremium}
     * @return Страховая премия
     */
    public int getInsurancePremium() {
        return insurancePremium;
    }

    /**
     * Функция получения значение поля {@link Policy#insuranceAmount}
     * @return Страховая сумма
     */
    public int getInsuranceAmount() {
        return insuranceAmount;
    }

    /**
     * Функция получения значение поля {@link Policy#dateOfConclusion}
     * @return Дата заключения
     */
    public @NotNull LocalDate getDateOfConclusion() {
        return dateOfConclusion;
    }

    /**
     * Функция получения значение поля {@link Policy#expirationDate}
     * @return Дата окончания действия
     */
    public @NotNull LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Функция получения значение поля {@link Policy#policyholderId}
     * @return Id страхователя
     */
    public int getPolicyholderId() {
        return policyholderId;
    }

    /**
     * Функция получения значение поля {@link Policy#carId}
     * @return Id автомобиля
     */
    public int getCarId() {
        return carId;
    }

    /**
     * Функция получения значение поля {@link Policy#employeeId}
     * @return Id сотрудника
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Функция получения значение поля {@link Policy#policyholderName}
     * @return Имя страхователя
     */
    public String getPolicyholderName() {
        return policyholderName;
    }

    /**
     * Функция получения значение поля {@link Policy#carModel}
     * @return Модель автомобиля
     */
    public String getCarModel() {
        return carModel;
    }

    /**
     * Функция получения значение поля {@link Policy#employeeName}
     * @return Имя сотрудника
     */
    public String getEmployeeName() {
        return employeeName;
    }
}