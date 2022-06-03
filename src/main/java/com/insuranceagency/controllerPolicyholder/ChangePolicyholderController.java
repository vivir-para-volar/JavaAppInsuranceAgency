package com.insuranceagency.controllerPolicyholder;

import com.insuranceagency.database.DBPolicyholder;
import com.insuranceagency.model.Policyholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

/**
 * Класс Контроллер для представления <b>changePolicyholder.fxml</b>.
 * <p>Данный класс предназначен для изменения страхователя.</p>
 */
public class ChangePolicyholderController {
    @FXML
    private TextField tfSearch;
    @FXML
    private  TextField tfFullName;
    @FXML
    private  TextField tfBirthday;
    @FXML
    private  TextField tfTelephone;
    @FXML
    private  TextField tfPassport;

    private Policyholder policyholder;

    private Stage dialogStage;
    /**
     * Метод для передачи данных при загрузке сцены
     * @param addStage Текущая сцена
     * @param policyholderId Id страхователя
     */
    public void setAddStage(Stage addStage, int policyholderId) {
        this.dialogStage = addStage;

        try {
            policyholder = DBPolicyholder.searchPolicyholderID(policyholderId);
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
     * Заполняет поля информацией о выбранном страхователе
     */
    private void fillInfo(){
        tfFullName.setText(policyholder.getFullName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        tfBirthday.setText(policyholder.getBirthday().format(formatter));

        tfTelephone.setText(policyholder.getTelephone());
        tfPassport.setText(policyholder.getPassport());
    }

    /**
     * Поиск страхователя в БД по номеру телефона или паспорту при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            policyholder = DBPolicyholder.searchPolicyholderTelephoneOrPassport(search);
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

        String telephone = tfTelephone.getText().trim();
        if (telephone.isEmpty()) {
            throw new Exception("Заполните поле Номер телефона");
        }
        if (telephone.length() > 15) {
            throw new Exception("Номер телефона не может быть больше 15 символов");
        }
        for (var i = 0; i < telephone.length(); i++) {
            if (!Character.isDigit(telephone.charAt(i))) {
                throw new Exception("Номер телефона должен содержать только цифры");
            }
        }

        String passport = tfPassport.getText().trim();
        if (passport.isEmpty()) {
            throw new Exception("Заполните поле Паспорт");
        }
        if (passport.length() != 10) {
            throw new Exception("Паспорт должен содержать 10 цифр");
        }
        for (var i = 0; i < passport.length(); i++) {
            if (!Character.isDigit(passport.charAt(i))) {
                throw new Exception("Паспорт должен содержать только цифры");
            }
        }

        policyholder = new Policyholder(policyholder.getId(), fullName, policyholder.getBirthday(), telephone, passport);
    }

    /**
     * Вызов метода изменения страхователя в БД по нажатию на кнопку "Изменить"
     */
    public void onChange(ActionEvent actionEvent) {
        try {
            if (policyholder == null) throw new Exception("Страхователь не выбран");

            readDate();
            DBPolicyholder.changePolicyholder(policyholder);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Страхователь успешно изменён");
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
     * Вызов метода удаления страхователя из БД по нажатию на кнопку "Удалить"
     */
    public void onDelete(ActionEvent actionEvent) {
        try {
            if (policyholder == null) throw new Exception("Страхователь не выбран");

            DBPolicyholder.deletePolicyholder(policyholder.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Страхователь успешно удалён");
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
        policyholder = null;

        tfFullName.clear();
        tfBirthday.clear();
        tfTelephone.clear();
        tfPassport.clear();
    }
}
