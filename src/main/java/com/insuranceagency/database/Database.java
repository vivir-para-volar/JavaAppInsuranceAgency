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
import java.util.ArrayList;

/**
 * Класс для взаимодействия с БД
 */
public class Database {
    /**
     * URL БД
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
     * Функция получения значение поля {@link Database#user}
     * @return Текущей пользователь
     */
    public static Employee getUser() {
        return user;
    }

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
        //String query = String.format("SELECT * FROM employees WHERE login = '%s' AND password = '%s'", login, getHash(password));
        String query = String.format("SELECT * FROM employees WHERE id = 2");

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
     * Формирует финансовый отчёт деятельности компании за заданный период времени
     * @param insuranceType Вид страхования
     * @param dateStart Дата начала
     * @param dateEnd Дата окончания
     * @return Сформированный отчёт
     */
    public static ArrayList<Integer> createReport(String insuranceType, LocalDate dateStart, LocalDate dateEnd) throws Exception{
        var resultList =new ArrayList<Integer>();

        String query;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (insuranceType.equals("ОСАГО и КАСКО"))
        {
            query = String.format("SELECT " +
                                  "(SELECT SUM(insurancePayment) FROM insuranceEvents " +
                                  "WHERE date >= '%s' AND date <= '%s'), " +

                                  "COUNT(id), SUM(insurancePremium) FROM policies " +
                                  "WHERE dateOfConclusion >= '%s' AND DateOfConclusion <= '%s'",

                                    dateStart.format(formatter), dateEnd.format(formatter),
                                    dateStart.format(formatter), dateEnd.format(formatter));
        }
        else
        {
            query = String.format("SELECT " +
                                  "(SELECT SUM(insurancePayment) " +
                                  "FROM policies as p LEFT JOIN insuranceEvents as ie ON p.id = ie.policyId " +
                                  "WHERE p.insuranceType = '%s' AND date >= '%s' AND Date <= '%s'), " +

                                  "COUNT(id), SUM(insurancePremium) FROM policies " +
                                  "WHERE insuranceType = '%s' AND dateOfConclusion >= '%s' AND dateOfConclusion <= '%s'",

                                    insuranceType, dateStart.format(formatter), dateEnd.format(formatter),
                                    insuranceType, dateStart.format(formatter), dateEnd.format(formatter));
        }

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            resultList.add(resultSet.getInt(2));
            resultList.add(resultSet.getInt(3));
            resultList.add(resultSet.getInt(1));

            return resultList;
        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }
}