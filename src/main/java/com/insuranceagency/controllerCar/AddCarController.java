package com.insuranceagency.controllerCar;

import com.insuranceagency.database.DBCar;
import com.insuranceagency.database.DBPhoto;
import com.insuranceagency.model.Car;

import com.insuranceagency.model.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Класс Контроллер для представления <b>addCar.fxml</b>.
 * <p>Данный класс предназначен для добавления автомобиля.</p>
 */
public class AddCarController {
    @FXML
    private TextField tfModel;
    @FXML
    private TextField tfVin;
    @FXML
    private TextField tfRegistrationPlate;
    @FXML
    private TextField tfVehiclePassport;

    @FXML
    private ImageView ivPhoto;
    @FXML
    private Label lbUploadDate;
    @FXML
    private Button btnLast;
    @FXML
    private Button btnNext;


    private ArrayList<Image> listImages;
    private ArrayList<File> listFiles;
    private int currentIndex;

    @FXML
    void initialize() {
        listImages = new ArrayList<>();
        listFiles = new ArrayList<>();
        currentIndex = 0;
    }

    private Car carForPolicy;
    /**
     * Получение добавленного автомобиля
     * @return Добавленный автомобиль
     */
    public Car getCarForPolicy(){
        return carForPolicy;
    }

    private Stage dialogStage;
    /**
     * Метод для передачи данных при загрузке сцены
     * @param addStage Текущая сцена
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     * @return Сформированный автомобиль
     */
    private Car readDate() throws Exception {
        String model = tfModel.getText().trim();
        if (model.isEmpty()) {
            throw new Exception("Заполните поле Модель");
        }

        String vin = tfVin.getText().trim();
        if (vin.length() != 17)
        {
            throw new Exception("VIN номер должен содержать 17 знаков");
        }

        String registrationPlate = tfRegistrationPlate.getText().trim();
        if (registrationPlate.isEmpty())
        {
            throw new Exception("Заполните поле Регистрационный знак");
        }

        String vehiclePassport = tfVehiclePassport.getText().trim();
        if (vehiclePassport.isEmpty()) {
            throw new Exception("Заполните поле Паспорт ТС");
        }
        if (vehiclePassport.length() != 10) {
            throw new Exception("Паспорт ТС должен содержать 10 символов");
        }
        for (var i = 0; i < vehiclePassport.length(); i++) {
            if ((i == 0 || i == 1) && !Character.isDigit(vehiclePassport.charAt(i)))
            {
                throw new Exception("Первые два символа серии паспорта ТС должны быть цифрами");
            }
            if ((i == 2 || i == 3) && !Character.isLetter(vehiclePassport.charAt(i)))
            {
                throw new Exception("Последние два символа серии паспорта ТС должны быть буквами");
            }
            if (i > 3 && !Character.isDigit(vehiclePassport.charAt(i)))
            {
                throw new Exception("Номер паспорта ТС должен содержать только цифры");
            }
        }

        return new Car(model, vin, registrationPlate, vehiclePassport);
    }

    /**
     * Вызов метода добавления автомобиля в БД по нажатию на кнопку "Добавить"
     */
    public void onAdd(ActionEvent actionEvent) {
        try
        {
            if(listImages.size() == 0) throw new Exception("Добавьте фотографию автомобиля");

            Car car = readDate();
            DBCar.addCar(car);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Автомобиль успешно добавлен");
            alert.showAndWait();

            Car newCar = DBCar.searchCarVin(car.getVin());
            InteractionPhoto.createDirectory(newCar.getId());

            ArrayList<Photo> listPhotos = new ArrayList<>();
            for (var i = 0; i < listImages.size(); i++) {
                File file = listFiles.get(i);

                Photo photo = new Photo(file.getName(), LocalDate.now(), newCar.getId());
                listPhotos.add(photo);

                InteractionPhoto.loadPhotoInDirectory(newCar.getId(), file);
            }
            DBPhoto.addPhotos(listPhotos);

            if(dialogStage != null ){
                carForPolicy = newCar;
                dialogStage.close();
            } else {
                clear();
            }
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Очищает поля на форме
     */
    private void clear(){
        tfModel.clear();
        tfVin.clear();
        tfRegistrationPlate.clear();
        tfVehiclePassport.clear();

        listImages.clear();
        listFiles.clear();
        currentIndex = 0;
        ivPhoto.setImage(null);
        lbUploadDate.setText("");
        btnLast.setVisible(false);
        btnNext.setVisible(false);
    }

    /**
     * Добавление фотографии при нажатии на кнопку "Загрузить"
     */
    public void onAddPhoto(ActionEvent actionEvent) {
        File file = InteractionPhoto.loadPhoto();

        if(file != null) {
            Image image = new Image(file.toURI().toString());
            listFiles.add(file);

            listImages.add(image);
            ivPhoto.setImage(image);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            lbUploadDate.setText(LocalDate.now().format(dtf));

            if (listImages.size() == 2) {
                btnLast.setVisible(true);
                btnNext.setVisible(true);
            }
        }
    }

    /**
     * Листает слайдер с изображениями назад
     */
    public void onLast(ActionEvent actionEvent) {
        if(currentIndex == 0) currentIndex = listImages.size() - 1;
        else currentIndex--;

        ivPhoto.setImage(listImages.get(currentIndex));
    }
    /**
     * Листает слайдер с изображениями вперёд
     */
    public void onNext(ActionEvent actionEvent) {
        if(currentIndex == listImages.size() - 1) currentIndex = 0;
        else currentIndex++;

        ivPhoto.setImage(listImages.get(currentIndex));
    }
}
