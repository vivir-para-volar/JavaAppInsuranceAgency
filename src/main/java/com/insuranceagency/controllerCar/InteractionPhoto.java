package com.insuranceagency.controllerCar;

import com.insuranceagency.model.Photo;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Класс для взаимодействия с фотографиями
 */
public class InteractionPhoto {
    final static String pathDirectoryForAllPhotos = String.valueOf(Paths.get("./photos"));

    /**
     * Создание папки для хранения фотографий определенного атомобиля
     * @param carId Название папки (Id автомобиля)
     */
    public static void createDirectory(int carId) throws Exception {
        String pathDirectory = pathDirectoryForAllPhotos + carId;
        File directoryCarPhotos = new File(pathDirectory);
        boolean result = directoryCarPhotos.mkdirs();
        if(!result){
            throw new Exception("Не удалось создать папку для хранения фотографий автомобиля");
        }
    }

    /**
     * Удаление папки с фотографиями определенного атомобиля
     * @param carId Название папки (Id автомобиля)
     */
    public static void deleteDirectory(int carId) throws Exception {
        String pathDirectory = pathDirectoryForAllPhotos + carId;
        File directoryCarPhotos = new File(pathDirectory);
        if(directoryCarPhotos.exists()) {
            for (File file : directoryCarPhotos.listFiles()) {
                file.delete();
            }
            boolean result = directoryCarPhotos.delete();
            if (!result) {
                throw new Exception("Не удалось удалить папку с фотографиями автомобиля");
            }
        }
    }

    /**
     * Загрузка фотографии
     * @return Загруженная фотография
     */
    public static File loadPhoto(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор фотографии");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("All image files", "*.png", "*.jpg");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("JPEG files", "*.jpg");
        FileChooser.ExtensionFilter filter3 = new FileChooser.ExtensionFilter("PNG image Files", "*.png");
        fileChooser.getExtensionFilters().addAll(filter1, filter2, filter3);
        File file = fileChooser.showOpenDialog(null);
        return file;
    }

    /**
     * Загрузка фотографии в папку
     * @param carId Название папки (Id автомобиля)
     * @param file Изображение для загрузки
     */
    public static void loadPhotoInDirectory(int carId, File file){
        Path path = Paths.get(pathDirectoryForAllPhotos + "/" + (carId) + "/" + file.getName());
        try {
            Files.copy(file.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Найти фото
     * @param photo Фото
     * @return Найденное изображение
     */
    public static Image searchPhoto(Photo photo){
        Path path = Paths.get(pathDirectoryForAllPhotos + "/" + (photo.getCarId()) + "/" + photo.getPath());
        Image image = new Image(path.toUri().toString());
        return image;
    }
}
