package com.insuranceagency.database;

import com.insuranceagency.model.PersonAllowedToDrive;
import com.insuranceagency.model.Policy;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Класс для взаимодействия с БД с таблицей Полисы
 */
public class DBPolicy {

    /**
     * Получение всего списка полисов
     * @return Список полисов
     */
    public static ArrayList<Policy> allPolicies() throws Exception {
        var resultList = new ArrayList<Policy>();

        String query = "SELECT * FROM policies ORDER BY dateOfConclusion DESC";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String insuranceType = resultSet.getString("insuranceType");
                int insurancePremium = resultSet.getInt("insurancePremium");
                int insuranceAmount = resultSet.getInt("insuranceAmount");

                String dateOfConclusionTemp = resultSet.getString("dateOfConclusion");
                LocalDate dateOfConclusion = LocalDate.parse(dateOfConclusionTemp, formatter);

                String expirationDateTemp = resultSet.getString("expirationDate");
                LocalDate expirationDate = LocalDate.parse(expirationDateTemp, formatter);

                int policyholderId = resultSet.getInt("policyholderId");
                int carId = resultSet.getInt("carId");
                int employeeId = resultSet.getInt("employeeId");

                var policy = new Policy(id, insuranceType, insurancePremium, insuranceAmount, dateOfConclusion, expirationDate, policyholderId, carId, employeeId);
                policy.searchName();
                resultList.add(policy);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Добавление полиса в БД
     * @param policy Полис
     */
    public static void addPolicyWithConnections(@NotNull Policy policy, @NotNull ArrayList<PersonAllowedToDrive> listPersonAllowedToDrive) throws Exception {
        if (policy == null) throw new Exception("Полис не выбран");
        if (listPersonAllowedToDrive == null || listPersonAllowedToDrive.size() == 0) throw new Exception("Список лиц, допущенных к управлению, не выбран");

        String query1 = String.format("SELECT id FROM policyholders WHERE id = %d", policy.getPolicyholderId());
        String query2 = String.format("SELECT id FROM cars WHERE id = %d", policy.getCarId());
        String query3 = String.format("SELECT id FROM employees WHERE id = %d", policy.getEmployeeId());
        String query4 = String.format("SELECT MAX(expirationDate) FROM policies WHERE insuranceType = '%s' AND carId = '%d'",
                                        policy.getInsuranceType(), policy.getCarId());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String queryP1 = String.format("INSERT INTO policies(insuranceType, insurancePremium, insuranceAmount, dateOfConclusion, expirationDate, policyholderId, carId, employeeId) " +
                        "VALUES('%s', %d, %d, '%s', '%s', %d, %d, %d); ",
                policy.getInsuranceType(),
                policy.getInsurancePremium(),
                policy.getInsuranceAmount(),
                policy.getDateOfConclusion().format(formatter),
                policy.getExpirationDate().format(formatter),
                policy.getPolicyholderId(),
                policy.getCarId(),
                policy.getEmployeeId());

        String queryP2 = "SET @id := LAST_INSERT_ID();";

        StringBuilder queryP3 = new StringBuilder("INSERT INTO connections(policyId, personAllowedToDriveId) VALUES ");
        int size = listPersonAllowedToDrive.size();
        for (var i = 0; i < size - 1; i++)
        {
            queryP3.append(String.format("(@id, %d), ", listPersonAllowedToDrive.get(i).getId()));
        }
        queryP3.append(String.format("(@id, %d); ", listPersonAllowedToDrive.get(size - 1).getId()));


        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow == 0) {
                flag = true;
                throw new Exception("Данный страхователь не существует");
            }

            resultSet = statement.executeQuery(query2);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow == 0) {
                flag = true;
                throw new Exception("Данный автомобиль не существует");
            }

            resultSet = statement.executeQuery(query3);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow == 0) {
                flag = true;
                throw new Exception("Данный сотрудник не существует");
            }

            resultSet = statement.executeQuery(query4);
            resultSet.next();
            String dateTemp = resultSet.getString(1);
            if(dateTemp != null) {
                LocalDate date = LocalDate.parse(dateTemp, formatter);
                if(date.isAfter(policy.getDateOfConclusion()) || date.isEqual(policy.getDateOfConclusion())){
                    flag = true;
                    throw new Exception("Нельзя оформить полис на заданный период, так как уже действует другой");
                }
            }


            // Сброс автофиксации
            connection.setAutoCommit(false);
            // Первый запрос
            PreparedStatement updateSales = connection.prepareStatement(queryP1);
            updateSales.executeUpdate();
            // Второй запрос
            updateSales = connection.prepareStatement(queryP2);
            updateSales.executeUpdate();
            // Третий запрос
            updateSales = connection.prepareStatement(queryP3.toString());
            updateSales.executeUpdate();
            // Завершение транзакции
            connection.commit();
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Изменение полиса в БД
     * @param policy Полис
     * @param listPersonAllowedToDrive Список лиц, допущенных к управлению
     */
    public static void changePolicyWithConnections(@NotNull Policy policy, @NotNull ArrayList<PersonAllowedToDrive> listPersonAllowedToDrive) throws Exception {
        if (policy == null) throw new Exception("Полис не выбран");
        if (listPersonAllowedToDrive == null  || listPersonAllowedToDrive.size() == 0) throw new Exception("Список лиц, допущенных к управлению, не выбран");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String queryP1 = String.format("UPDATE policies SET insurancePremium = %d, expirationDate = '%s' WHERE id = %d; ",
                policy.getInsurancePremium(),
                policy.getExpirationDate().format(formatter),
                policy.getId());

        String queryP2 = String.format("DELETE FROM connections WHERE policyId = %d; ", policy.getId());

        StringBuilder queryP3 = new StringBuilder("INSERT INTO connections(policyId, personAllowedToDriveId) VALUES ");
        int size = listPersonAllowedToDrive.size();
        for (var i = 0; i < size - 1; i++)
        {
            queryP3.append(String.format("(%d, %d), ", policy.getId(), listPersonAllowedToDrive.get(i).getId()));
        }
        queryP3.append(String.format("(%d, %d); ", policy.getId(), listPersonAllowedToDrive.get(size - 1).getId()));

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            // Сброс автофиксации
            connection.setAutoCommit(false);
            // Первый запрос
            PreparedStatement updateSales = connection.prepareStatement(queryP1);
            updateSales.executeUpdate();
            // Второй запрос
            PreparedStatement deleteSales = connection.prepareStatement(queryP2);
            deleteSales.executeUpdate();
            // Третий запрос
            updateSales = connection.prepareStatement(queryP3.toString());
            updateSales.executeUpdate();
            // Завершение транзакции
            connection.commit();
        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск списка полисов по Id страхователя
     * @param policyholderId Id страхователя
     * @return Список найденных полисов
     */
    public static ArrayList<Policy> searchPolicyPolicyholderId(int policyholderId) throws Exception{
        var resultList = new ArrayList<Policy>();

        String query = String.format("SELECT * FROM policies WHERE policyholderID = %d ORDER BY dateOfConclusion DESC", policyholderId);

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String insuranceType = resultSet.getString("insuranceType");
                int insurancePremium = resultSet.getInt("insurancePremium");
                int insuranceAmount = resultSet.getInt("insuranceAmount");

                String dateOfConclusionTemp = resultSet.getString("dateOfConclusion");
                LocalDate dateOfConclusion = LocalDate.parse(dateOfConclusionTemp, formatter);

                String expirationDateTemp = resultSet.getString("expirationDate");
                LocalDate expirationDate = LocalDate.parse(expirationDateTemp, formatter);

                int carId = resultSet.getInt("carId");
                int employeeId = resultSet.getInt("employeeId");

                var policy = new Policy(id, insuranceType, insurancePremium, insuranceAmount, dateOfConclusion, expirationDate, policyholderId, carId, employeeId);
                policy.searchName();
                resultList.add(policy);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск полиса по Id
     * @param id Id полиса
     * @return Найденный полис
     */
    public static Policy searchPolicyID(int id) throws Exception {
        String query = String.format("SELECT * FROM policies WHERE id = %d", id);

        boolean flag = false;
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()) {
                String insuranceType = resultSet.getString("insuranceType");
                int insurancePremium = resultSet.getInt("insurancePremium");
                int insuranceAmount = resultSet.getInt("insuranceAmount");

                String dateOfConclusionTemp = resultSet.getString("dateOfConclusion");
                LocalDate dateOfConclusion = LocalDate.parse(dateOfConclusionTemp, formatter);

                String expirationDateTemp = resultSet.getString("expirationDate");
                LocalDate expirationDate = LocalDate.parse(expirationDateTemp, formatter);

                int policyholderId = resultSet.getInt("policyholderId");
                int carId = resultSet.getInt("carId");
                int employeeId = resultSet.getInt("employeeId");

                return new Policy(id, insuranceType, insurancePremium, insuranceAmount, dateOfConclusion, expirationDate, policyholderId, carId, employeeId);
            }

            flag = true;
            throw new Exception("Данный полис не существует");

        }catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }
}
