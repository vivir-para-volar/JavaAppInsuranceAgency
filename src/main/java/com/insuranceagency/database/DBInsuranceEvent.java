package com.insuranceagency.database;

import com.insuranceagency.model.InsuranceEvent;

import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBInsuranceEvent {

    /**
     * Добавление страхового случая в БД
     * @param insuranceEvent Страховой случай
     */
    public static void addInsuranceEvent(@NotNull InsuranceEvent insuranceEvent) throws Exception {
        if (insuranceEvent == null) throw new Exception("Страховой случай не выбран");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String query = String.format("INSERT INTO insuranceEvents(date, insurancePayment, description, policyId) VALUES('%s', %d, '%s', %d)",
                insuranceEvent.getDate().format(formatter),
                insuranceEvent.getInsurancePayment(),
                insuranceEvent.getDescription(),
                insuranceEvent.getPolicyId());

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск списка страховых случаев по Id полиса
     * @param policyId Id полиса
     * @return Список найденных страховых случаев
     */
    public static ArrayList<InsuranceEvent> searchInsuranceEvent(int policyId) throws Exception {
        var resultList = new ArrayList<InsuranceEvent>();

        String query = String.format("SELECT * FROM insuranceEvents WHERE policyId = %d ORDER BY date", policyId);

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()){
                int id = resultSet.getInt("id");

                String dateTemp = resultSet.getString("date");
                LocalDate date = LocalDate.parse(dateTemp, formatter);

                int insurancePayment = resultSet.getInt("insurancePayment");
                String description = resultSet.getString("description");

                var insuranceEvent = new InsuranceEvent(id, date, insurancePayment, description, policyId);
                resultList.add(insuranceEvent);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск максимальной даты страхового случая по Id полиса
     * @param policyId Id полиса
     * @return Максимальная дата
     */
    public static LocalDate searchInsuranceEventMaxDate(int policyId) throws Exception {
        LocalDate date;

        String query = String.format("SELECT MAX(date) FROM insuranceEvents WHERE policyId = %d", policyId);

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            String dateTemp = resultSet.getString(1);
            if(dateTemp != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                date = LocalDate.parse(dateTemp, formatter);
                return date;
            }
            return null;
        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }
}
