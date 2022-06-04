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
 * Класс Контроллер для представления <b>changeCar.fxml</b>.
 * <p>Данный класс предназначен для изменения автомобиля.</p>
 */
public class ChangeCarController {
    @FXML
    private TextField tfSearch;
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
    private ArrayList<LocalDate> listData;
    private ArrayList<File> listNewFiles;
    private int currentIndex;

    private Car car;

    @FXML
    void initialize() {
        listImages = new ArrayList<>();
        listData = new ArrayList<>();
        listNewFiles = new ArrayList<>();
        currentIndex = 0;
    }

    private Stage dialogStage;
    /**
     * Метод для передачи данных при загрузке сцены
     * @param addStage Текущая сцена
     * @param carId Id автомобиля
     */
    public void setAddStage(Stage addStage, int carId) {
        this.dialogStage = addStage;

        try {
            car = DBCar.searchCarID(carId);
            fillInfo();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Заполняет поля информацией о выбранном автомобиле
     */
    private void fillInfo() throws Exception {
        tfModel.setText(car.getModel());
        tfVin.setText(car.getVin());
        tfRegistrationPlate.setText(car.getRegistrationPlate());
        tfVehiclePassport.setText(car.getVehiclePassport());

        ArrayList<Photo> list = DBPhoto.searchPhotos(car.getId());
        if(list.size() != 0) {
            for (var photo : list) {
                Image img = InteractionPhoto.searchPhoto(photo);
                listImages.add(img);
                listData.add(photo.getUploadDate());
            }
            ivPhoto.setImage(listImages.get(0));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            lbUploadDate.setText(listData.get(0).format(dtf));

            if (listImages.size() >= 2) {
                btnLast.setVisible(true);
                btnNext.setVisible(true);
            }
        }
    }

    /**
     * Поиск автомобиля в БД по VIN номеру при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            car = DBCar.searchCarVin(search);
            fillInfo();
            tfSearch.clear();
        } catch (Exception exc) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exc.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     */
    private void readDate() throws Exception {
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

        car = new Car(car.getId(), car.getModel(), car.getVin(), registrationPlate, vehiclePassport);
    }

    /**
     * Вызов метода изменения автомобиля в БД по нажатию на кнопку "Изменить"
     */
    public void onChange(ActionEvent actionEvent) {
        try {
            if (car == null) throw new Exception("Аатомобиль не выбран");

            readDate();
            DBCar.changeCar(car);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Автомобиль успешно изменён");
            alert.showAndWait();

            ArrayList<Photo> listPhotos = new ArrayList<>();
            for (var i = 0; i < listNewFiles.size(); i++) {
                File file = listNewFiles.get(i);

                Photo photo = new Photo(file.getName(), LocalDate.now(), car.getId());
                listPhotos.add(photo);

                InteractionPhoto.loadPhotoInDirectory(car.getId(), file);
            }
            DBPhoto.addPhotos(listPhotos);

            if(dialogStage != null ) dialogStage.close();
            else clear();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Вызов метода удаления автомобиля из БД по нажатию на кнопку "Удалить"
     */
    public void onDelete(ActionEvent actionEvent) {
        try {
            if (car == null) throw new Exception("Автомобиль не выбран");

            InteractionPhoto.deleteDirectory(car.getId());
            DBCar.deleteCar(car.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Автомобиль успешно удалён");
            alert.showAndWait();

            if(dialogStage != null ) dialogStage.close();
            else clear();
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
        car = null;

        tfModel.clear();
        tfVin.clear();
        tfRegistrationPlate.clear();
        tfVehiclePassport.clear();


        listImages.clear();
        listNewFiles.clear();
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
            listNewFiles.add(file);

            listImages.add(image);
            listData.add(LocalDate.now());
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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        lbUploadDate.setText(listData.get(currentIndex).format(dtf));
    }
    /**
     * Листает слайдер с изображениями вперёд
     */
    public void onNext(ActionEvent actionEvent) {
        if(currentIndex == listImages.size() - 1) currentIndex = 0;
        else currentIndex++;

        ivPhoto.setImage(listImages.get(currentIndex));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        lbUploadDate.setText(listData.get(currentIndex).format(dtf));
    }
}
