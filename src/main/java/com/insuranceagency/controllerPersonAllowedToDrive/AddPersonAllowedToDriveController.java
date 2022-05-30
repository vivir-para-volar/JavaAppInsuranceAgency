package com.insuranceagency.controllerPersonAllowedToDrive;

import com.insuranceagency.database.DBPersonAllowedToDrive;
import com.insuranceagency.model.PersonAllowedToDrive;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddPersonAllowedToDriveController {
    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfDrivingLicence;

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     * @return Сформированное лицо, допущенное к управлению
     */
    private PersonAllowedToDrive ReadDate() throws Exception {
        String fullName = tfFullName.getText().trim();
        if (fullName == "") {
            throw new Exception("Заполните поле ФИО");
        }

        String drivingLicence = tfDrivingLicence.getText().trim();
        if (drivingLicence == "") {
            throw new Exception("Заполните поле Водительское удостоверение");
        }
        if (drivingLicence.length() != 10) {
            throw new Exception("Водительское удостоверение должен содержать 10 цифр");
        }
        for (var i = 0; i < drivingLicence.length(); i++) {
            if (!Character.isDigit(drivingLicence.charAt(i))) {
                throw new Exception("Водительское удостоверение должно содержать только цифры");
            }
        }

        PersonAllowedToDrive personAllowedToDrive = new PersonAllowedToDrive(fullName, drivingLicence);
        return personAllowedToDrive;
    }

    /**
     * Вызов метода добавления лица, допущенного к управлению, в БД по нажатию на кнопку "Добавить"
     */
    public void onAdd(ActionEvent actionEvent) {
        try
        {
            PersonAllowedToDrive personAllowedToDrive = ReadDate();
            DBPersonAllowedToDrive.addPersonAllowedToDrive(personAllowedToDrive);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Лицо, допущенное к управлению, успешно добавлено");
            alert.showAndWait();

            clear();
        } catch (Exception exc) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exc.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Очищает поля на форме
     */
    private void clear(){
        tfFullName.clear();
        tfDrivingLicence.clear();
    }
}
