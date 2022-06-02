package com.insuranceagency.controllerEmployee;

import com.insuranceagency.Main;
import com.insuranceagency.database.DBEmployee;
import com.insuranceagency.model.Employee;

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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AllEmployeesController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<Employee> tableEmployees;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> fullNameColumn;
    @FXML
    private TableColumn<Employee, String> birthdayColumn;
    @FXML
    private TableColumn<Employee, String> telephoneColumn;
    @FXML
    private TableColumn<Employee, String> passportColumn;
    @FXML
    private TableColumn<Employee, String> loginColumn;

    private Employee selectedEmployee;

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
        loginColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getLogin()));

        fillTable();
    }

    /**
     * Заполнение таблицы
     */
    private void fillTable(){
        tableEmployees.getItems().clear();

        try{
            ArrayList<Employee> listEmployees = DBEmployee.allEmployees();
            ObservableList<Employee> list = FXCollections.observableArrayList(listEmployees);
            tableEmployees.setItems(list);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }

        tableEmployees.getSelectionModel().selectedItemProperty().addListener((observableValue, employee, e1) -> {
            if (e1 != null) {
                selectedEmployee = e1;
            }
        });
    }

    /**
     * Поиск сотрудника в БД по номеру телефона или паспорту при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            Employee employee = DBEmployee.searchEmployeeTelephoneOrPassport(search);
            tableEmployees.setItems(FXCollections.observableArrayList(employee));
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
     * По нажатию на кнопку "Изменить" проверяет выбран ли сотрудник и вызывает метод показа сцены изменения
     */
    public void onChangeEmployee(ActionEvent actionEvent) {
        if(tableEmployees.isFocused() && selectedEmployee != null) {
            showDialogChange(selectedEmployee);
            fillTable();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Выберите сотрудника");
            alert.showAndWait();
        }
    }

    /**
     * Показ сцены изменения сотрудника
     * @param employee Сотрудник для изменения
     */
    private void showDialogChange(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/employee/changeEmployee.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Изменение сотрудника");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            ChangeEmployeeController controller = loader.getController();
            controller.setAddStage(addStage, employee.getId());
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
