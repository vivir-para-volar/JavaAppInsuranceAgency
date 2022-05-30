package com.insuranceagency.database;

import com.insuranceagency.model.Car;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

public class DBCar {

    /**
     * Получение всего списка автомобилей
     * @return Список автомобилей
     */
    public static ArrayList<Car> allCars() throws Exception {
        var resultList = new ArrayList<Car>();

        String query = "SELECT * FROM cars";

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String model = resultSet.getString("model");
                String vin = resultSet.getString("vin");
                String registrationPlate = resultSet.getString("registrationPlate");
                String vehiclePassport = resultSet.getString("vehiclePassport");

                var car = new Car(id, model, vin, registrationPlate, vehiclePassport);
                resultList.add(car);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Добавление автомобиля в БД
     * @param car Автомобиль
     */
    public static void addCar(@NotNull Car car) throws Exception {
        if (car == null) throw new Exception("Автомобиль не выбран");

        String query1 = String.format("SELECT id FROM cars WHERE vin = '%s'", car.getVin());

        String query = String.format("INSERT INTO cars(model, vin, registrationPlate, vehiclePassport) VALUES('%s', '%s', '%s', '%s'); ",
                car.getModel(),
                car.getVin(),
                car.getRegistrationPlate(),
                car.getVehiclePassport());

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный VIN номер уже используется");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Изменение автомобиля в БД
     * @param car Автомобиль
     */
    public static void changeCar(@NotNull Car car) throws Exception {
        if (car == null) throw new Exception("Автомобиль не выбран");

        String query = String.format("UPDATE cars SET registrationPlate = '%s', vehiclePassport = '%s' WHERE id = '%d'",
                car.getRegistrationPlate(),
                car.getVehiclePassport(),
                car.getId());

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Удаление автомобиля из БД
     * @param id Id автомобиля
     */
    public static void deleteCar(int id) throws Exception {
        String query1 = String.format("SELECT id FROM policies WHERE carId = %d", id);

        String query = String.format("DELETE FROM cars WHERE id = %d", id);

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Вы не можете удалить данный автомобили, так как на него оформлен полис");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск автомобиля по VIN номеру
     * @param vin VIN номер
     * @return Найденный автомобиль
     */
    public static Car searchCarVin(@NotNull String vin) throws Exception{
        String query = String.format("SELECT * FROM cars WHERE vin = '%s'", vin);
        return searchCar(query);
    }

    /**
     * Поиск автомобиля по Id
     * @param id Id автомобиля
     * @return Найденный автомобиль
     */
    public static Car searchCarID(int id) throws Exception {
        String query = String.format("SELECT * FROM cars WHERE id = %d", id);
        return searchCar(query);
    }

    /**
     * Поиск автомобиля по определённому запросу
     * @param query Запрос
     * @return Найденный автомобиль
     */
    private static Car searchCar(@NotNull String query) throws Exception{
        if (query == null) throw new Exception("Запрос не выбран");

        boolean flag = false;
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int countRow = 0;
            while (resultSet.next()) {
                countRow++;

                int id = resultSet.getInt("id");
                String model = resultSet.getString("model");
                String vin = resultSet.getString("vin");
                String registrationPlate = resultSet.getString("registrationPlate");
                String vehiclePassport = resultSet.getString("vehiclePassport");

                var car = new Car(id, model, vin, registrationPlate, vehiclePassport);
                return car;
            }

            if (countRow == 0) {
                flag = true;
                throw new Exception("Данный автомобиль не существует");
            }
            return null;

        }catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }
}
