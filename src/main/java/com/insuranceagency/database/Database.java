package com.insuranceagency.database;

import com.insuranceagency.model.Employee;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Database {
    /**
     * Адрес БД
     */
    final static String DB_URL = "jdbc:mysql://localhost:3306/InsuranceAgency";
    /**
     * Логин для входа в БД
     */
    final static String LOGIN = "root";
    /**
     * Пароль для входа в БД
     * */
    final static String PASSWORD = "root";

    /**
     * Текущий пользователь
     */
    private static Employee user;

    /**
     * Получение хеша строки
     * @param input Строка для хеширования
     * @return Хеш строки
     */
    public static String getHash(String input) throws Exception {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes(Charset.forName("UTF-8")),0,input.length());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        }catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Авторизация пользователя
     * @param login Логин
     * @param password Пароль
     */
    public static void authorization(String login, String password) throws Exception {
        String query = String.format("SELECT * FROM employees WHERE login = '%s' AND password = '%s'", login, getHash(password));

        boolean flag = false;
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int countRow = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()) {
                countRow++;

                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");

                String birthdayTemp = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(birthdayTemp, formatter);

                String telephone = resultSet.getString("telephone");
                String passport = resultSet.getString("passport");

                user = new Employee(id, fullName, birthday, telephone, passport, login, password);
            }

            if (countRow == 0) {
                flag = true;
                throw new Exception("Неправильно указан логин и/или пароль");
            }
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Функция получения значение поля {@link Database#user}
     * @return Текущей пользователь
     */
    public static Employee getUser() {
        return user;
    }

    /**
     * Функция изменения значение поля {@link Database#user}
     * @param user Текущей пользователь
     */
    public static void setUser(Employee user) {
        Database.user = user;
    }
}