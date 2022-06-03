package com.insuranceagency.controllerPolicy;

import com.insuranceagency.database.*;
import com.insuranceagency.model.*;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Класс Контроллер для представления <b>detailPolicy.fxml</b>.
 * <p>Данный класс предназначен для представления подробной информации о полисе.</p>
 */
public class DetailPolicyController {
    @FXML
    private Label lbInsuranceType;
    @FXML
    private Label lbInsurancePremium;
    @FXML
    private Label lbInsuranceAmount;
    @FXML
    private Label lbDateOfConclusion;
    @FXML
    private Label lbExpirationDate;
    @FXML
    private Label lbPolicyholder;
    @FXML
    private Label lbCar;
    @FXML
    private Label lbEmployee;

    @FXML
    private TableView<PersonAllowedToDrive> tablePersonsAllowedToDrive;
    @FXML
    private TableColumn<PersonAllowedToDrive, String> fullNameColumn;
    @FXML
    private TableColumn<PersonAllowedToDrive, String> drivingLicenceColumn;

    @FXML
    private TableView<InsuranceEvent> tableInsuranceEvents;
    @FXML
    private TableColumn<InsuranceEvent, String> dateColumn;
    @FXML
    private TableColumn<InsuranceEvent, Integer> insurancePaymentColumn;
    @FXML
    private TableColumn<InsuranceEvent, String> descriptionColumn;

    private Policy policy;

    @FXML
    void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        fullNameColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getFullName()));
        drivingLicenceColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getDrivingLicence()));

        dateColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getDate().format(dtf)));
        insurancePaymentColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getInsurancePayment()));
        descriptionColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getDescription()));
    }

    /**
     * Метод для передачи данных при загрузке сцены
     * @param policyId Id полиса
     */
    public void setAddStage(int policyId) {
        try {
            policy = DBPolicy.searchPolicyID(policyId);
            fill();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Заполнение
     */
    private void fill(){
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            lbInsuranceType.setText(policy.getInsuranceType());
            lbInsurancePremium.setText(String.valueOf(policy.getInsurancePremium()));
            lbInsuranceAmount.setText(String.valueOf(policy.getInsuranceAmount()));
            lbDateOfConclusion.setText(policy.getDateOfConclusion().format(dtf));
            lbExpirationDate.setText(policy.getExpirationDate().format(dtf));

            Policyholder policyholder = DBPolicyholder.searchPolicyholderID(policy.getPolicyholderId());
            lbPolicyholder.setText(String.format("%s (%s)", policyholder.getFullName(), policyholder.getTelephone()));

            Car car = DBCar.searchCarID(policy.getCarId());
            lbCar.setText(String.format("%s (%s)", car.getModel(), car.getVin()));

            Employee employee = DBEmployee.searchEmployeeID(policy.getEmployeeId());
            lbEmployee.setText(String.format("%s (%s)", employee.getFullName(), employee.getTelephone()));

            ArrayList<PersonAllowedToDrive> listPersonsAllowedToDrive = DBPersonAllowedToDrive.searchPersonsAllowedToDrivePolicyId(policy.getId());
            ObservableList<PersonAllowedToDrive> list1 = FXCollections.observableArrayList(listPersonsAllowedToDrive);
            tablePersonsAllowedToDrive.setItems(list1);

            ArrayList<InsuranceEvent> listInsuranceEvent = DBInsuranceEvent.searchInsuranceEvent(policy.getId());
            ObservableList<InsuranceEvent> list2 = FXCollections.observableArrayList(listInsuranceEvent);
            tableInsuranceEvents.setItems(list2);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }
}
