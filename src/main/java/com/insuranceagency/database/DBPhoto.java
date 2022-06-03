package com.insuranceagency.database;

import com.insuranceagency.model.Photo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Класс для взаимодействия с БД с таблицей Фотографии
 */
public class DBPhoto {

    /**
     * Поиск фотографий определённого автомобиля
     * @param carId Id автомобиля
     * @return Список найденных фотографий
     */
    private static ArrayList<Photo> searchPhotos(int carId) throws Exception{
        var resultList = new ArrayList<Photo>();

        String query = String.format("SELECT * FROM photos WHERE carId = '%d'", carId);

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String path = resultSet.getString("path");

                String uploadDateTemp = resultSet.getString("uploadDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate uploadDate = LocalDate.parse(uploadDateTemp, formatter);

                var photo = new Photo(id, path, uploadDate, carId);
                resultList.add(photo);
            }

            return resultList;
        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }
}
