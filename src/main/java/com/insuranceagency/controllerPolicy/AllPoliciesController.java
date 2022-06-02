package com.insuranceagency.controllerPolicy;

import com.insuranceagency.Main;
import com.insuranceagency.MainController;
import com.insuranceagency.database.DBPolicy;
import com.insuranceagency.database.DBPolicyholder;
import com.insuranceagency.model.Policy;
import com.insuranceagency.model.Policyholder;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AllPoliciesController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<Policy> tablePolicies;
    @FXML
    private TableColumn<Policy, Integer> idColumn;
    @FXML
    private TableColumn<Policy, String> insuranceTypeColumn;
    @FXML
    private TableColumn<Policy, Integer> insurancePremiumColumn;
    @FXML
    private TableColumn<Policy, Integer> insuranceAmountColumn;
    @FXML
    private TableColumn<Policy, String> dateOfConclusionColumn;
    @FXML
    private TableColumn<Policy, String> expirationDateColumn;
    @FXML
    private TableColumn<Policy, String> policyholderColumn;
    @FXML
    private TableColumn<Policy, String> carColumn;
    @FXML
    private TableColumn<Policy, String> employeeColumn;

    private Policy selectedPolicy;

    @FXML
    void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        idColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getId()));
        insuranceTypeColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getInsuranceType()));
        insurancePremiumColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getInsurancePremium()));
        insuranceAmountColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getInsuranceAmount()));
        dateOfConclusionColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getDateOfConclusion().format(dtf)));
        expirationDateColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getExpirationDate().format(dtf)));
        policyholderColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getPolicyholderName()));
        carColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getCarModel()));
        employeeColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getEmployeeName()));

        fillTable();
    }

    /**
     * Заполнение таблицы
     */
    private void fillTable(){
        tablePolicies.getItems().clear();

        try{
            ArrayList<Policy> listPolicies = DBPolicy.allPolicies();
            ObservableList<Policy> listPolicy = FXCollections.observableArrayList(listPolicies);
            tablePolicies.setItems(listPolicy);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }

        tablePolicies.getSelectionModel().selectedItemProperty().addListener((observableValue, policy, p1) -> {
            if (p1 != null) {
                selectedPolicy = p1;
            }
        });
    }

    /**
     * Поиск всех полисов в БД по номеру телефона или паспорту страхователя при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            Policyholder policyholder = DBPolicyholder.searchPolicyholderTelephoneOrPassport(search);

            ArrayList<Policy> listPolicies = DBPolicy.searchPolicyPolicyholderId(policyholder.getId());
            ObservableList<Policy> listPolicy = FXCollections.observableArrayList(listPolicies);
            tablePolicies.setItems(listPolicy);
            tfSearch.clear();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * По нажатию на кнопку "Изменить" проверяет выбран ли полис и вызывает метод показа сцены изменения
     */
    public void onChangePolicy(ActionEvent actionEvent) {
        if(tablePolicies.isFocused() && selectedPolicy != null) {
            showDialogChange(selectedPolicy);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Выберите полис");
            alert.showAndWait();
        }
    }
    /**
     * Показ сцены изменения полиса
     * @param policy Выбранный полис
     */
    private void showDialogChange(Policy policy) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("view/policy/changePolicy.fxml"));
            Parent page = loader.load();
            MainController.getBorderPane.setCenter(page);
            ChangePolicyController controller = loader.getController();
            controller.setAddStage(false, policy.getId());
        } catch (IOException exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }

    /**
     * По нажатию на кнопку "Подробнее" проверяет выбран ли полис и вызывает метод показа сцены подробной информации
     */
    public void onDetailPolicy(ActionEvent actionEvent) {
        if(tablePolicies.isFocused() && selectedPolicy != null) {
            showDialogDetail(selectedPolicy);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Выберите полис");
            alert.showAndWait();
        }
    }
    /**
     * Показ сцены подробной информации о полисе
     * @param policy Выбранный полис
     */
    private void showDialogDetail(Policy policy) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policy/detailPolicy.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Подробнее о полисе");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            DetailPolicyController controller = loader.getController();
            controller.setAddStage(policy.getId());
            addStage.showAndWait();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }

    /**
     * По нажатию на кнопку "Исходное состояние" возвращает таблицу в первоночальный вид
     */
    public void onClear(ActionEvent actionEvent) {
        fillTable();
    }
}
