package com.insuranceagency.database;

import com.insuranceagency.model.Policy;
import com.insuranceagency.model.Policyholder;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBPolicy {

    /**
     * Получение всего списка полисов
     * @return Список полисов
     */
    public static ArrayList<Policy> allPolicies() throws Exception {
        var resultList = new ArrayList<Policy>();

        String query = "SELECT * FROM policy ORDER BY dateOfConclusion DESC";

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
                resultList.add(policy);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Добавление страхователя в БД
     * @param policyholder Страхователь
     */
    public static void addPolicyholder(@NotNull Policyholder policyholder) throws Exception {
        if (policyholder == null) throw new Exception("Страхователь не выбран");

        String query1 = String.format("SELECT id FROM policyholders WHERE telephone = '%s'", policyholder.getTelephone());
        String query2 = String.format("SELECT id FROM policyholders WHERE passport = '%s'", policyholder.getPassport());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String query = String.format("INSERT INTO policyholders (fullName, birthday, telephone, passport) VALUES('%s', '%s', '%s', '%s')",
                policyholder.getFullName(),
                policyholder.getBirthday().format(formatter),
                policyholder.getTelephone(),
                policyholder.getPassport());

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный телефон уже используется");
            }

            resultSet = statement.executeQuery(query2);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный паспорт уже используется");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Изменение страхователя в БД
     * @param policyholder Страхователь
     */
    public static void changePolicyholder(@NotNull Policyholder policyholder) throws Exception {
        if (policyholder == null) throw new Exception("Страхователь не выбран");

        String query1 = String.format("SELECT id FROM policyholders WHERE telephone = '%s' AND id <> %d",
                policyholder.getTelephone(),
                policyholder.getId());
        String query2 = String.format("SELECT id FROM policyholders WHERE passport = '%s' AND id <> %d",
                policyholder.getPassport(),
                policyholder.getId());

        String query = String.format("UPDATE policyholders SET fullName = '%s', telephone = '%s', passport = '%s' WHERE id = %d",
                policyholder.getFullName(),
                policyholder.getTelephone(),
                policyholder.getPassport(),
                policyholder.getId());

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный телефон уже используется");
            }

            resultSet = statement.executeQuery(query2);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный паспорт уже используется");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск списка полисов по Id страхователя
     * @param policyholderId Id страхователя
     * @return Список найденных полисов
     */
    public static ArrayList<Policy> searchPolicyPolicyholderId(int policyholderId) throws Exception{
        var resultList = new ArrayList<Policy>();

        String query = String.format("SELECT * FROM policies WHERE policyholderID = '%d' ORDER BY dateOfConclusion DESC", policyholderId);

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

            int countRow = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()) {
                countRow++;

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
                return policy;
            }

            if (countRow == 0) {
                flag = true;
                throw new Exception("Данный полис не существует");
            }
            return null;

        }catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }
}
