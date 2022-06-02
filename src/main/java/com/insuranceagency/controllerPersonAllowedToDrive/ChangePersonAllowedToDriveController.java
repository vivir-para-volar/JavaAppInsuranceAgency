package com.insuranceagency.controllerPersonAllowedToDrive;

import com.insuranceagency.database.DBPersonAllowedToDrive;
import com.insuranceagency.model.PersonAllowedToDrive;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangePersonAllowedToDriveController {
    @FXML
    private TextField tfSearch;
    @FXML
    private  TextField tfFullName;
    @FXML
    private  TextField tfDrivingLicence;

    private PersonAllowedToDrive personAllowedToDrive;

    private Stage dialogStage;
    public void setAddStage(Stage addStage, int id) {
        this.dialogStage = addStage;

        try {
            personAllowedToDrive = DBPersonAllowedToDrive.searchPersonAllowedToDriveID(id);
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
     * Заполняет поля информацией о выбранном лице, допущенном к управлению
     */
    private void fillInfo(){
        tfFullName.setText(personAllowedToDrive.getFullName());
        tfDrivingLicence.setText(personAllowedToDrive.getDrivingLicence());
    }

    /**
     * Поиск лица, допущенного к управлению, в БД по водительскому удостоверению при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            personAllowedToDrive = DBPersonAllowedToDrive.searchPersonAllowedToDriveDrivingLicence(search);
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
        String fullName = tfFullName.getText().trim();
        if (fullName.isEmpty()) {
            throw new Exception("Заполните поле ФИО");
        }

        String drivingLicence = tfDrivingLicence.getText().trim();
        if (drivingLicence.isEmpty()) {
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

        personAllowedToDrive = new PersonAllowedToDrive(personAllowedToDrive.getId(), fullName, drivingLicence);
    }

    /**
     * Вызов метода изменения лица, допущенного к управлению, в БД по нажатию на кнопку "Изменить"
     */
    public void onChange(ActionEvent actionEvent) {
        try {
            if (personAllowedToDrive == null) throw new Exception("Лицо, допущенное к управлению, не выбрано");

            readDate();
            DBPersonAllowedToDrive.changePersonAllowedToDrive(personAllowedToDrive);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Лицо, допущенное к управлению, успешно изменёно");
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
     * Вызов метода удаления лица, допущенного к управлению, из БД по нажатию на кнопку "Удалить"
     */
    public void onDelete(ActionEvent actionEvent) {
        try {
            if (personAllowedToDrive == null) throw new Exception("Лицо, допущенное к управлению, не выбрано");

            DBPersonAllowedToDrive.deletePersonAllowedToDrive(personAllowedToDrive.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Лицо, допущенное к управлению, успешно удалёно");
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
        personAllowedToDrive = null;

        tfFullName.clear();
        tfDrivingLicence.clear();
    }
}
