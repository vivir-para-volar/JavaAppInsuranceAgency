package com.insuranceagency.controllerCar;

import com.insuranceagency.database.DBCar;
import com.insuranceagency.model.Car;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddCarController {
    @FXML
    private TextField tfModel;
    @FXML
    private TextField tfVin;
    @FXML
    private TextField tfRegistrationPlate;
    @FXML
    private TextField tfVehiclePassport;

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     * @return Сформированный автомобиль
     */
    private Car ReadDate() throws Exception {
        String model = tfModel.getText().trim();
        if (model == "") {
            throw new Exception("Заполните поле Модель");
        }

        String vin = tfVin.getText().trim();
        if (vin.length() != 17)
        {
            throw new Exception("VIN номер должен содержать 17 знаков");
        }

        String registrationPlate = tfRegistrationPlate.getText().trim();
        if (registrationPlate == "")
        {
            throw new Exception("Заполните поле Регистрационный знак");
        }

        String vehiclePassport = tfVehiclePassport.getText().trim();
        if (vehiclePassport == "") {
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
            boolean temp = Character.isLetter(vehiclePassport.charAt(i));
            if ((i == 2 || i == 3) && !Character.isLetter(vehiclePassport.charAt(i)))
            {
                throw new Exception("Последние два символа серии паспорта ТС должны быть буквами");
            }
            if (i > 3 && !Character.isDigit(vehiclePassport.charAt(i)))
            {
                throw new Exception("Номер паспорта ТС должен содержать только цифры");
            }
        }

        var car = new Car(model, vin, registrationPlate, vehiclePassport);
        return car;
    }

    /**
     * Вызов метода добавления автомобиля в БД по нажатию на кнопку "Добавить"
     */
    public void onAdd(ActionEvent actionEvent) {
        try
        {
            Car car = ReadDate();
            DBCar.addCar(car);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Автомобиль успешно добавлен");
            alert.showAndWait();

            clear();
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
    }
}
