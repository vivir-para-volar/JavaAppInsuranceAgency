package com.insuranceagency.controllerPolicy;

import com.insuranceagency.Main;
import com.insuranceagency.database.DBPolicy;
import com.insuranceagency.database.DBPolicyholder;
import com.insuranceagency.model.Policy;
import com.insuranceagency.model.Policyholder;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PolicyController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<Policyholder> tablePolicyholders;
    @FXML
    private TableColumn<Policyholder, Integer> idPolicyholderColumn;
    @FXML
    private TableColumn<Policyholder, String> fullNameColumn;
    @FXML
    private TableColumn<Policyholder, String> birthdayColumn;
    @FXML
    private TableColumn<Policyholder, String> telephoneColumn;
    @FXML
    private TableColumn<Policyholder, String> passportColumn;


    @FXML
    private TableView<Policy> tablePolicies;
    @FXML
    private TableColumn<Policy, Integer> idPolicyColumn;
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
    private TableColumn<Policy, String> carColumn;
    @FXML
    private TableColumn<Policy, String> employeeColumn;


    private Policyholder selectedPolicyholder;
    private Policy selectedPolicy;

    @FXML
    void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        idPolicyholderColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getId()));
        fullNameColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getFullName()));
        birthdayColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getBirthday().format(dtf)));
        telephoneColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getTelephone()));
        passportColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getPassport()));

        idPolicyColumn.setCellValueFactory(param -> new
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
        carColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getCarModel()));
        employeeColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getEmployeeName()));
    }

    /**
     * Заполнение таблицы
     */
    private void fillTables(){
        tablePolicyholders.getItems().clear();
        tablePolicyholders.getItems().clear();

        try{
            ObservableList<Policyholder> listPolicyholder = FXCollections.observableArrayList(selectedPolicyholder);
            tablePolicyholders.setItems(listPolicyholder);

            ArrayList listPolicies = DBPolicy.searchPolicyPolicyholderId(selectedPolicyholder.getId());
            ObservableList<Policy> listPolicy = FXCollections.observableArrayList(listPolicies);
            tablePolicies.setItems(listPolicy);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }

        tablePolicyholders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Policyholder>() {
            @Override
            public void changed(ObservableValue<? extends Policyholder> observableValue, Policyholder policyholder, Policyholder p1) {
                if (p1 != null) {
                    selectedPolicyholder = p1;
                }
            }
        });
    }

    /**
     * Поиск страхователя в БД по номеру телефона или паспорту и всех его полисов при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            selectedPolicyholder = DBPolicyholder.searchPolicyholderTelephoneOrPassport(search);
            fillTables();
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
     * По нажатию на кнопку "Добавить" проверяет выбран страховаттель и вызывает метод показа сцены добавления
     */
    public void onAdd(ActionEvent actionEvent) {
        if(selectedPolicyholder != null) {
            showDialogAdd(selectedPolicyholder);
            fillTables();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Выберите страхователя");
            alert.showAndWait();
        }
    }

    /**
     * Показ сцены добавления полиса
     * @param policyholder Выбранный страхователь
     */
    private void showDialogAdd(Policyholder policyholder) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policy/addPolicy.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Добавление полиса");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            AddPolicyChooseCarControlle controller = loader.getController();
            controller.setAddStage(addStage, policyholder.getId());
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
     * По нажатию на кнопку "Изменить" проверяет выбран ли полис и вызывает метод показа сцены изменения
     */
    public void onChange(ActionEvent actionEvent) {
        if(tablePolicies.isFocused() && selectedPolicy != null) {
            showDialogChange(selectedPolicy);
            fillTables();
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
     * @param policy Полис для изменения
     */
    private void showDialogChange(Policy policy) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policy/changePolicy.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Изменение полиса");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            ChangePolicyController controller = loader.getController();
            controller.setAddStage(addStage, policy.getId());
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
     * По нажатию на кнопку "Очистить" очищает форму
     */
    public void onClear(ActionEvent actionEvent) {
        selectedPolicyholder = null;
        selectedPolicy = null;

        tablePolicyholders.getItems().clear();
        tablePolicyholders.getItems().clear();

    }
}
