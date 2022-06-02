package com.insuranceagency.database;

import com.insuranceagency.model.PersonAllowedToDrive;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBPersonAllowedToDrive {

    /**
     * Получение всего списка лиц, допущенных к управлению
     * @return Список лиц, допущенных к управлению
     */
    public static ArrayList<PersonAllowedToDrive> allPersonsAllowedToDrive() throws Exception {
        var resultList = new ArrayList<PersonAllowedToDrive>();

        String query = "SELECT * FROM personsAllowedToDrive ORDER BY fullName";

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");
                String drivingLicence = resultSet.getString("drivingLicence");

                var personAllowedToDrive = new PersonAllowedToDrive(id, fullName, drivingLicence);
                resultList.add(personAllowedToDrive);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Добавление лица, допущенного к управлению, в БД
     * @param personAllowedToDrive Лицо, допущенное к управлению
     */
    public static void addPersonAllowedToDrive(@NotNull PersonAllowedToDrive personAllowedToDrive) throws Exception {
        if (personAllowedToDrive == null) throw new Exception("Лицо, допущенное к управлению, не выбрано");

        String query1 = String.format("SELECT id FROM personsAllowedToDrive WHERE drivingLicence  = '%s'",
                personAllowedToDrive.getDrivingLicence());

        String query = String.format("INSERT INTO personsAllowedToDrive(fullName, drivingLicence) VALUES('%s', '%s')",
                personAllowedToDrive.getFullName(),
                personAllowedToDrive.getDrivingLicence());

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данное водительское удостоверение уже используется");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Изменение лица, допущенного к управлению, в БД
     * @param personAllowedToDrive Лицо, допущенное к управлению
     */
    public static void changePersonAllowedToDrive(@NotNull PersonAllowedToDrive personAllowedToDrive) throws Exception {
        if (personAllowedToDrive == null) throw new Exception("Лицо, допущенное к управлению, не выбрано");

        String query1 = String.format("SELECT id FROM personsAllowedToDrive WHERE drivingLicence = '%s' AND id <> %d",
                personAllowedToDrive.getDrivingLicence(),
                personAllowedToDrive.getId());

        String query = String.format("UPDATE personsAllowedToDrive SET fullName = '%s', drivingLicence = '%s' WHERE id = %d",
                personAllowedToDrive.getFullName(),
                personAllowedToDrive.getDrivingLicence(),
                personAllowedToDrive.getId());

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данное водительское удостоверение уже используется");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Удаление лица, допущенного к управлению, из БД
     * @param id Id лица, допущенного к управлению
     */
    public static void deletePersonAllowedToDrive(int id) throws Exception {
        String query1 = String.format("SELECT * FROM connections WHERE personAllowedToDriveId = %d", id);

        String query = String.format("DELETE FROM personsAllowedToDrive WHERE id = %d", id);

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Вы не можете удалить данное лицо, допущенное к управлению, так как на него оформлен полис");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск лица, допущенного к управлению, по водительскому удостоверению
     * @param drivingLicence Водительское удостоверение
     * @return Найденное лицо, допущенное к управлению
     */
    public static PersonAllowedToDrive searchPersonAllowedToDriveDrivingLicence(@NotNull String drivingLicence) throws Exception{
        if(drivingLicence == null || drivingLicence.isEmpty()) throw new Exception("Водительское удостоверение лица, допущенного к управлению, не выбрано");

        String query = String.format("SELECT * FROM personsAllowedToDrive WHERE drivingLicence = '%s'", drivingLicence);
        return searchPersonAllowedToDrive(query);
    }

    /**
     * Поиск лица, допущенного к управлению, по Id
     * @param id Id лица, допущенного к управлению
     * @return Найденное лицо, допущенное к управлению
     */
    public static PersonAllowedToDrive searchPersonAllowedToDriveID(int id) throws Exception {
        String query = String.format("SELECT * FROM personsAllowedToDrive WHERE id = %d", id);
        return searchPersonAllowedToDrive(query);
    }

    /**
     * Поиск лица, допущенного к управлению, по определённому запросу
     * @param query Запрос
     * @return Найденное лицо, допущенное к управлению
     */
    private static PersonAllowedToDrive searchPersonAllowedToDrive(@NotNull String query) throws Exception{
        if (query == null || query.isEmpty()) throw new Exception("Запрос не выбран");

        boolean flag = false;
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");
                String drivingLicence = resultSet.getString("drivingLicence");

                return new PersonAllowedToDrive(id, fullName, drivingLicence);
            }

            flag = true;
            throw new Exception("Данный страхователь не существует");

        }catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск списка лиц, допущенных к управлению, по Id полиса
     * @param policyId Id полиса
     * @return Список лиц, допущенных к управлению
     */
    public static ArrayList<PersonAllowedToDrive> searchPersonsAllowedToDrivePolicyId(int policyId) throws Exception {
        var resultList = new ArrayList<PersonAllowedToDrive>();

        String query = String.format("SELECT * FROM connections WHERE policyId = %d", policyId);

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int personAllowedToDriveId = resultSet.getInt("personAllowedToDriveId");

                var personAllowedToDrive = searchPersonAllowedToDriveID(personAllowedToDriveId);
                resultList.add(personAllowedToDrive);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }
}