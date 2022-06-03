package com.insuranceagency.controllerPolicy;

import com.insuranceagency.database.DBPersonAllowedToDrive;
import com.insuranceagency.model.PersonAllowedToDrive;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Класс Контроллер для представления <b>choosePersonsAllowedToDrive.fxml</b>.
 * <p>Данный класс предназначен для выбора списка лиц, допущенных к управлению, закрепленных за полисом.</p>
 */
public class ChoosePersonsAllowedToDriveController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<PersonAllowedToDrive> tablePersonsAllowedToDrive;
    @FXML
    private TableColumn<PersonAllowedToDrive, Integer> idColumn;
    @FXML
    private TableColumn<PersonAllowedToDrive, String> fullNameColumn;
    @FXML
    private TableColumn<PersonAllowedToDrive, String> drivingLicenceColumn;

    @FXML
    private TableView<PersonAllowedToDrive> tableSelectedPersonsAllowedToDrive;
    @FXML
    private TableColumn<PersonAllowedToDrive, Integer> selectedIdColumn;
    @FXML
    private TableColumn<PersonAllowedToDrive, String> selectedFullNameColumn;
    @FXML
    private TableColumn<PersonAllowedToDrive, String> selectedDrivingLicenceColumn;

    private PersonAllowedToDrive selectedPersonInTable;
    private PersonAllowedToDrive selectedPersonInTableSelected;

    private ArrayList<PersonAllowedToDrive> listPersonAllowedToDrive;
    public ArrayList<PersonAllowedToDrive> getListPersonAllowedToDrive(){
        return listPersonAllowedToDrive;
    }

    @FXML
    void initialize() {
        listPersonAllowedToDrive = new ArrayList<>();

        idColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getId()));
        fullNameColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getFullName()));
        drivingLicenceColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getDrivingLicence()));

        selectedIdColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getId()));
        selectedFullNameColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getFullName()));
        selectedDrivingLicenceColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getDrivingLicence()));

        fillTable();
    }

    private Stage dialogStage;
    /**
     * Метод для передачи данных при загрузке сцены
     * @param addStage Текущая сцена
     * @param listPersonAllowedToDriveArray Список лиц, допущенных к управлению, закрепленных за полисом
     */
    public void setAddStage(Stage addStage, ArrayList<PersonAllowedToDrive> listPersonAllowedToDriveArray) {
        this.dialogStage = addStage;

        if(listPersonAllowedToDriveArray != null){
            this.listPersonAllowedToDrive.addAll(listPersonAllowedToDriveArray);
            ObservableList<PersonAllowedToDrive> list = FXCollections.observableArrayList(listPersonAllowedToDrive);
            tableSelectedPersonsAllowedToDrive.setItems(list);
        }
    }

    /**
     * Заполнение таблицы
     */
    private void fillTable(){
        tablePersonsAllowedToDrive.getItems().clear();

        try{
            ArrayList<PersonAllowedToDrive> listPersonAllowedToDrive = DBPersonAllowedToDrive.allPersonsAllowedToDrive();
            ObservableList<PersonAllowedToDrive> list = FXCollections.observableArrayList(listPersonAllowedToDrive);
            tablePersonsAllowedToDrive.setItems(list);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }

        tablePersonsAllowedToDrive.getSelectionModel().selectedItemProperty().addListener((observableValue, personAllowedToDrive, p1) -> {
            if (p1 != null) {
                selectedPersonInTable = p1;
            }
        });

        tableSelectedPersonsAllowedToDrive.getSelectionModel().selectedItemProperty().addListener((observableValue, personAllowedToDrive, p1) -> {
            if (p1 != null) {
                selectedPersonInTableSelected = p1;
            }
        });
    }

    /**
     * Поиск лица, допущенного к управлению, в БД по водительскому удостоверению при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            PersonAllowedToDrive personAllowedToDrive = DBPersonAllowedToDrive.searchPersonAllowedToDriveDrivingLicence(search);

            tablePersonsAllowedToDrive.getItems().clear();
            tablePersonsAllowedToDrive.getItems().add(personAllowedToDrive);
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
     * По нажатию на кнопку "Добавить в список" проверяет выбрано ли лицо, допущенное к управлению,
     * и добавляет его в таблицу выбранных
     */
    public void onAddSelectedPersonAllowedToDrive(ActionEvent actionEvent) {
        try {
            if (tablePersonsAllowedToDrive.isFocused() && selectedPersonInTable != null) {
                if (listPersonAllowedToDrive.contains(selectedPersonInTable)) {
                    throw new Exception("Данное лицо, допущенное к управлению, уже выбрано");
                }
                listPersonAllowedToDrive.add(selectedPersonInTable);
                tableSelectedPersonsAllowedToDrive.getItems().add(selectedPersonInTable);
            } else {
                throw new Exception("Выберите лицо, допущенное к управлению");
            }
        }
        catch (Exception exp){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * По нажатию на кнопку "Исходное состояние" возвращает таблицу в первоночальный вид
     */
    public void onClear(ActionEvent actionEvent) {
        fillTable();
    }

    /**
     * По нажатию на кнопку "Удалить из списка" проверяет выбрано ли лицо, допущенное к управлению,
     * и удаляет его из таблицы выбранных
     */
    public void onDeleteSelectedPersonAllowedToDrive(ActionEvent actionEvent) {
        if(tableSelectedPersonsAllowedToDrive.isFocused() && selectedPersonInTableSelected!= null) {
            listPersonAllowedToDrive.remove(selectedPersonInTableSelected);

            ObservableList<PersonAllowedToDrive> list = FXCollections.observableArrayList(listPersonAllowedToDrive);
            tableSelectedPersonsAllowedToDrive.setItems(list);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Выберите лицо, допущенное к управлению");
            alert.showAndWait();
        }
    }

    /**
     * По нажатию на кнопку "Продолжить" закрывает текущую сцену
     */
    public void onChoose(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
