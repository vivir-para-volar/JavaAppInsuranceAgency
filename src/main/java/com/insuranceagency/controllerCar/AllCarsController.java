package com.insuranceagency.controllerCar;

import com.insuranceagency.Main;
import com.insuranceagency.database.DBCar;
import com.insuranceagency.model.Car;

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

import java.util.ArrayList;

public class AllCarsController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<Car> tableCars;
    @FXML
    private TableColumn<Car, Integer> idColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, String> vinColumn;
    @FXML
    private TableColumn<Car, String> registrationPlateColumn;
    @FXML
    private TableColumn<Car, String> vehiclePassportColumn;

    @FXML
    private Button btnChangeChoose;

    private Car selectedCar;

    private Car carForPolicy;
    public Car getCarForPolicy(){
        return carForPolicy;
    }

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getId()));
        modelColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getModel()));
        vinColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getVin()));
        registrationPlateColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getRegistrationPlate()));
        vehiclePassportColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getVehiclePassport()));

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
        tableCars.getItems().clear();

        try{
            ArrayList<Car> listCars = DBCar.allCars();
            ObservableList<Car> list = FXCollections.observableArrayList(listCars);
            tableCars.setItems(list);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }

        tableCars.getSelectionModel().selectedItemProperty().addListener((observableValue, car, c1) -> {
            if (c1 != null) {
                selectedCar = c1;
            }
        });
    }

    /**
     * Поиск автомобиля в БД по VIN номеру при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            Car car = DBCar.searchCarVin(search);
            tableCars.setItems(FXCollections.observableArrayList(car));
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
     * По нажатию на кнопку "Изменить/Выбрать" проверяет выбран ли автомобиль
     * и вызывает метод показа сцены изменения или закрытие сцены
     */
    public void onChangeChooseCar(ActionEvent actionEvent) {
        if(tableCars.isFocused() && selectedCar != null) {
            if(dialogStage != null ){
                carForPolicy = selectedCar;
                dialogStage.close();
            }
            else {
                showDialogChange(selectedCar);
                fillTable();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Выберите автомобиль");
            alert.showAndWait();
        }
    }

    /**
     * Показ сцены изменения автомобиля
     * @param car Автомобиль для изменения
     */
    private void showDialogChange(Car car) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/car/changeCar.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Изменение автомобиля");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            ChangeCarController controller = loader.getController();
            controller.setAddStage(addStage, car.getId());
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
