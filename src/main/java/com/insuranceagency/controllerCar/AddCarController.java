package com.insuranceagency.controllerCar;

import com.insuranceagency.database.DBCar;
import com.insuranceagency.model.Car;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            Car car = readDate();
            DBCar.addCar(car);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Автомобиль успешно добавлен");
            alert.showAndWait();

            if(dialogStage != null ){
                carForPolicy = DBCar.searchCarVin(car.getVin());
                dialogStage.close();
            }
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
        tfModel.clear();
        tfVin.clear();
        tfRegistrationPlate.clear();
        tfVehiclePassport.clear();
    }
}
