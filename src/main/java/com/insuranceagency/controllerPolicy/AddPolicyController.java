package com.insuranceagency.controllerPolicy;

import com.insuranceagency.database.Database;
import com.insuranceagency.model.Employee;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;

public class AddPolicyController {
    @FXML
    private TextField tfInsuranceType;
    @FXML
    private TextField tfInsurancePremium;
    @FXML
    private TextField tfInsuranceAmount;
    @FXML
    private DatePicker dpDateOfConclusion;
    @FXML
    private DatePicker dpExpirationDate;
    @FXML
    private TextField tfPolicyholder;
    @FXML
    private TextField tfCar;
    @FXML
    private TextField tfEmployee;

    private Employee selectedEmployee;

    @FXML
    void initialize() {
        selectedEmployee = Database.getUser();
        tfEmployee.setText(selectedEmployee.getFullName());
    }

    public void onAdd(ActionEvent actionEvent) {
    }
}
