package com.insuranceagency.model;

public class Connection {
    /**
     * Поле Id полиса
     */
    private int policyId;
    /**
     * Поле Id лица, допущенного к управлению
     */
    private int personAllowedToDriveId;

    /**
     * Инициализирует новый экземпляр класса Connection с заданными параметрами
     * @param policyId Id полиса
     * @param personAllowedToDriveId Id лица, допущенного к управлению
     */
    public Connection(int policyId, int personAllowedToDriveId) {
        this.policyId = policyId;
        this.personAllowedToDriveId = personAllowedToDriveId;
    }

    /**
     * Функция получения значение поля {@link Connection#policyId}
     * @return Id полиса
     */
    public int getPolicyId() {
        return policyId;
    }

    /**
     * Функция получения значение поля {@link Connection#personAllowedToDriveId}
     * @return Id лица, допущенного к управлению
     */
    public int getPersonAllowedToDriveId() {
        return personAllowedToDriveId;
    }
}