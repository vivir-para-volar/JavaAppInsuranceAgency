package com.insuranceagency.controllerPolicy;

import com.insuranceagency.database.DBPolicy;
import com.insuranceagency.model.Policy;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChangePolicyController {

    @FXML
    private Button btnCancel;

    private Policy policy;

    private Stage dialogStage;
    public void setAddStage(Stage addStage, int id) {
        this.dialogStage = addStage;
        btnCancel.setVisible(true);

        try {
            policy = DBPolicy.searchPolicyID(id);
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
     * Заполняет поля информацией о выбранном полисе
     */
    private void fillInfo(){
        /*tfFullName.setText(policyholder.getFullName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        tfBirthday.setText(policyholder.getBirthday().format(formatter));

        tfTelephone.setText(policyholder.getTelephone());
        tfPassport.setText(policyholder.getPassport());*/
    }
}
