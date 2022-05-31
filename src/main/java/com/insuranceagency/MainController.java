package com.insuranceagency;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {
    @FXML
    private BorderPane borderPane;

    private Parent page;
    private FXMLLoader loader;

    private void loader(String resourcePath){
        try {
            loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource(resourcePath));
            page = loader.load();
        } catch (IOException exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
        borderPane.setCenter(page);
    }

    // Полис
    public void onAddPolicyMenuItem(ActionEvent actionEvent) {
        loader("view/policy/addPolicy.fxml");
    }

    public void onChangePolicyMenuItem(ActionEvent actionEvent) {
        loader("view/policy/policy.fxml");
    }

    // Автомобиль
    public void onAddCarMenuItem(ActionEvent actionEvent) {
        loader("view/car/addCar.fxml");
    }

    public void onChangeCarMenuItem(ActionEvent actionEvent) {
        loader("view/car/changeCar.fxml");
    }

    public void onAllCarsMenuItem(ActionEvent actionEvent) {
        loader("view/car/allCars.fxml");
    }


    // Лицо, допущенное к управлению
    public void onAddPersonAllowedToDriveMenuItem(ActionEvent actionEvent) {
        loader("view/personAllowedToDrive/addPersonAllowedToDrive.fxml");
    }

    public void onChangePersonAllowedToDriveMenuItem(ActionEvent actionEvent) {
        loader("view/personAllowedToDrive/changePersonAllowedToDrive.fxml");
    }

    public void onAllPersonsAllowedToDriveMenuItem(ActionEvent actionEvent) {
        loader("view/personAllowedToDrive/allPersonsAllowedToDrive.fxml");
    }


    // Страхователь
    public void onAddPolicyholderMenuItem(ActionEvent actionEvent) {
        loader("view/policyholder/addPolicyholder.fxml");
    }

    public void onChangePolicyholderMenuItem(ActionEvent actionEvent) {
        loader("view/policyholder/changePolicyholder.fxml");
    }

    public void onAllPolicyholdersMenuItem(ActionEvent actionEvent) {
        loader("view/policyholder/allPolicyholders.fxml");
    }


    // Сотрудник
    public void onAddEmployeeMenuItem(ActionEvent actionEvent) {
        loader("view/employee/addEmployee.fxml");
    }

    public void onChangeEmployeeMenuItem(ActionEvent actionEvent) {
        loader("view/employee/changeEmployee.fxml");
    }

    public void onAllEmployeesMenuItem(ActionEvent actionEvent) {
        loader("view/employee/allEmployees.fxml");
    }


    // Отчёт
    public void onFinancialReportMenuItem(ActionEvent actionEvent) {
        loader("view/report/report.fxml");
    }
}
