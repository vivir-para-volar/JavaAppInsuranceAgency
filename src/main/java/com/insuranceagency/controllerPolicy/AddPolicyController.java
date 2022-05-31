package com.insuranceagency.controllerPolicy;

import com.insuranceagency.database.Database;
import com.insuranceagency.model.Car;
import com.insuranceagency.model.Employee;
import com.insuranceagency.model.Policyholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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

    private Policyholder policyholder;
    private Car car;
    private Employee selectedEmployee;

    @FXML
    void initialize() {
        selectedEmployee = Database.getUser();
        tfEmployee.setText(selectedEmployee.getFullName());
    }

    public void onAddPolicyholder(ActionEvent actionEvent) {
    }

    public void onChoosePolicyholder(ActionEvent actionEvent) {
    }

    public void onAddCar(ActionEvent actionEvent) {
    }

    public void onChooseCar(ActionEvent actionEvent) {
    }

    public void onAdd(ActionEvent actionEvent) {
    }
}
