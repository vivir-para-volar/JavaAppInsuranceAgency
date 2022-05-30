package com.insuranceagency.database;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
     * Логин текущего пользователя
     */
    private static int userId;

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
        String query = String.format("SELECT id FROM Employees WHERE Login = '%s' AND Password = '%s'", login, getHash(password));

        boolean flag = false;
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow == 0) {
                flag = true;
                throw new Exception("Неправильно указан логин и/или пароль");
            }

            while (resultSet.next()){
                userId = resultSet.getInt("id");
            }
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Функция получения значение поля {@link Database#userId}
     * @return Id текущего пользователя
     */
    public static int getUserId() {
        return userId;
    }

    /**
     * Функция изменения значение поля {@link Database#userId}
     * @param userId Id текущего пользователя
     */
    public static void setUserLogin(int userId) {
        Database.userId = userId;
    }
}