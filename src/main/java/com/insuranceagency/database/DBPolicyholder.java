package com.insuranceagency.database;

import com.insuranceagency.model.Policyholder;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

public class DBPolicyholder {

    /**
     * Получение всего списка страхователей
     * @return Список страхователей
     */
    public static ArrayList<Policyholder> allPolicyholders() throws Exception {
        var resultList = new ArrayList<Policyholder>();

        String query = "SELECT * FROM policyholders ORDER BY fullName";

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");

                String birthdayTemp = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(birthdayTemp, formatter);

                String telephone = resultSet.getString("telephone");
                String passport = resultSet.getString("passport");

                var policyholder = new Policyholder(id, fullName, birthday, telephone, passport);
                resultList.add(policyholder);
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
     * Удаление страхователя из БД
     * @param id Id страхователя
     */
    public static void deletePolicyholder(int id) throws Exception {
        String query1 = String.format("SELECT id FROM policies WHERE policyholderId = %d", id);

        String query = String.format("DELETE FROM policyholders WHERE id = %d", id);

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Вы не можете удалить данного страхователя, так как на него оформлен полис");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск страхователя по номеру телефона или паспорту
     * @param telephoneOrPassport Номер телефона или паспорт
     * @return Найденный страхователь
     */
    public static Policyholder searchPolicyholderTelephoneOrPassport(@NotNull String telephoneOrPassport) throws Exception{
        if(telephoneOrPassport == null || telephoneOrPassport.isEmpty()) throw new Exception("Телефон или паспорт страхователя не выбран");

        String query = String.format("SELECT * FROM policyholders WHERE telephone = '%s' OR passport = '%s'", telephoneOrPassport, telephoneOrPassport);
        return searchPolicyholder(query);
    }

    /**
     * Поиск страхователя по Id
     * @param id Id страхователя
     * @return Найденный страхователь
     */
    public static Policyholder searchPolicyholderID(int id) throws Exception {
        String query = String.format("SELECT * FROM policyholders WHERE id = %d", id);
        return searchPolicyholder(query);
    }

    /**
     * Поиск страхователя по определённому запросу
     * @param query Запрос
     * @return Найденный страхователь
     */
    private static Policyholder searchPolicyholder(@NotNull String query) throws Exception{
        if (query == null || query.isEmpty()) throw new Exception("Запрос не выбран");

        boolean flag = false;
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");

                String birthdayTemp = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(birthdayTemp, formatter);

                String telephone = resultSet.getString("telephone");
                String passport = resultSet.getString("passport");

                return new Policyholder(id, fullName, birthday, telephone, passport);
            }

            flag = true;
            throw new Exception("Данный страхователь не существует");

        }catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }
}