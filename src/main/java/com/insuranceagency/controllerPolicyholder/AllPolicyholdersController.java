package com.insuranceagency.controllerPolicyholder;

import com.insuranceagency.Main;
import com.insuranceagency.database.DBPolicyholder;
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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Класс Контроллер для представления <b>allPolicyholders.fxml</b>.
 * <p>Данный класс предназначен для представления всего списка страхователей.</p>
 */
public class AllPolicyholdersController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<Policyholder> tablePolicyholders;
    @FXML
    private TableColumn<Policyholder, Integer> idColumn;
    @FXML
    private TableColumn<Policyholder, String> fullNameColumn;
    @FXML
    private TableColumn<Policyholder, String> birthdayColumn;
    @FXML
    private TableColumn<Policyholder, String> telephoneColumn;
    @FXML
    private TableColumn<Policyholder, String> passportColumn;

    @FXML
    private Button btnChangeChoose;

    private Policyholder selectedPolicyholder;

    private Policyholder policyholderForPolicy;
    public Policyholder getPolicyholderForPolicy(){
        return policyholderForPolicy;
    }

    @FXML
    void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        idColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getId()));
        fullNameColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getFullName()));
        birthdayColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getBirthday().format(dtf)));
        telephoneColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getTelephone()));
        passportColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getPassport()));

        fillTable();
    }

    private Stage dialogStage;
    public void setAddStage(Stage addStage) {
        btnChangeChoose.setText("Выбрать");
        this.dialogStage = addStage;
    }

    /**
     * Заполнение таблицы
     */
    private void fillTable(){
        tablePolicyholders.getItems().clear();

        try{
            ArrayList<Policyholder> listPolicyholders = DBPolicyholder.allPolicyholders();
            ObservableList<Policyholder> list = FXCollections.observableArrayList(listPolicyholders);
            tablePolicyholders.setItems(list);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }

        tablePolicyholders.getSelectionModel().selectedItemProperty().addListener((observableValue, policyholder, p1) -> {
            if (p1 != null) {
                selectedPolicyholder = p1;
            }
        });
    }

    /**
     * Поиск страхователя в БД по номеру телефона или паспорту при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            Policyholder policyholder = DBPolicyholder.searchPolicyholderTelephoneOrPassport(search);
            tablePolicyholders.setItems(FXCollections.observableArrayList(policyholder));
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
     * По нажатию на кнопку "Изменить/Выбрать" проверяет выбран ли страхователь
     * и вызывает метод показа сцены изменения или закрытие сцены
     */
    public void onChangeChoosePolicyholder(ActionEvent actionEvent) {
        if(tablePolicyholders.isFocused() && selectedPolicyholder != null) {
            if(dialogStage != null ){
                policyholderForPolicy = selectedPolicyholder;
                dialogStage.close();
            }
            else {
                showDialogChange(selectedPolicyholder);
                fillTable();
            }
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
     * Показ сцены изменения страхователя
     * @param policyholder Страхователь для изменения
     */
    private void showDialogChange(Policyholder policyholder) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policyholder/changePolicyholder.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Изменение страхователя");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            ChangePolicyholderController controller = loader.getController();
            controller.setAddStage(addStage,policyholder.getId());
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