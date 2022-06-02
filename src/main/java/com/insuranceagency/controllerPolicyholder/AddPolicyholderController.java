package com.insuranceagency.controllerPolicyholder;

import com.insuranceagency.model.Policyholder;
import com.insuranceagency.database.DBPolicyholder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddPolicyholderController {
    @FXML
    private TextField tfFullName;
    @FXML
    private DatePicker dpBirthday;
    @FXML
    private TextField tfTelephone;
    @FXML
    private TextField tfPassport;

    private Policyholder policyholderForPolicy;
    public Policyholder getPolicyholderForPolicy(){
        return policyholderForPolicy;
    }

    @FXML
    void initialize() {
        dpBirthday.setValue(LocalDate.EPOCH);
    }

    private Stage dialogStage;
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     * @return Сформированный страхователь
     */
    private Policyholder readDate() throws Exception {
        String fullName = tfFullName.getText().trim();
        if (fullName.isEmpty()) {
            throw new Exception("Заполните поле ФИО");
        }

        LocalDate birthday = dpBirthday.getValue();

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

        return new Policyholder(fullName, birthday, telephone, passport);
    }

    /**
     * Вызов метода добавления страхователя в БД по нажатию на кнопку "Добавить"
     */
    public void onAdd(ActionEvent actionEvent) {
        try
        {
            Policyholder policyholder = readDate();
            DBPolicyholder.addPolicyholder(policyholder);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Страхователь успешно добавлен");
            alert.showAndWait();

            if(dialogStage != null ){
                policyholderForPolicy = DBPolicyholder.searchPolicyholderTelephoneOrPassport(policyholder.getPassport());
                dialogStage.close();
            }
            else clear();
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
        dpBirthday.setValue(LocalDate.EPOCH);
        tfTelephone.clear();
        tfPassport.clear();
    }
}