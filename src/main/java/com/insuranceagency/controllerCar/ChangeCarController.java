package com.insuranceagency.controllerCar;

import com.insuranceagency.database.DBCar;
import com.insuranceagency.model.Car;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private Car car;

    private Stage dialogStage;
    public void setAddStage(Stage addStage, int id) {
        this.dialogStage = addStage;

        try {
            car = DBCar.searchCarID(id);
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
    private void fillInfo(){
        tfModel.setText(car.getModel());
        tfVin.setText(car.getVin());
        tfRegistrationPlate.setText(car.getRegistrationPlate());
        tfVehiclePassport.setText(car.getVehiclePassport());
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
    }
}
