package com.insuranceagency.controllerPersonAllowedToDrive;

import com.insuranceagency.Main;
import com.insuranceagency.database.DBPersonAllowedToDrive;
import com.insuranceagency.model.PersonAllowedToDrive;
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

import java.util.ArrayList;

/**
 * Класс Контроллер для представления <b>allPersonsAllowedToDrive.fxml</b>.
 * <p>Данный класс предназначен для представления всего списка лиц, допущенных к управлению.</p>
 */
public class AllPersonsAllowedToDriveController {
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

    private PersonAllowedToDrive selectedPersonAllowedToDrive;

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(param -> new
                SimpleObjectProperty<>(param.getValue().getId()));
        fullNameColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getFullName()));
        drivingLicenceColumn.setCellValueFactory(param -> new
                SimpleStringProperty(param.getValue().getDrivingLicence()));

        fillTable();
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
                selectedPersonAllowedToDrive = p1;
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
            tablePersonsAllowedToDrive.setItems(FXCollections.observableArrayList(personAllowedToDrive));
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
     * По нажатию на кнопку "Изменить" проверяет выбрано ли лицо, допущенное к управлению, и вызывает метод показа сцены изменения
     */
    public void onChangePersonAllowedToDrive(ActionEvent actionEvent) {
        if(tablePersonsAllowedToDrive.isFocused() && selectedPersonAllowedToDrive != null) {
            showDialogChange(selectedPersonAllowedToDrive);
            fillTable();
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
     * Показ сцены изменения лица, допущенного к управлению
     * @param personAllowedToDrive Лицо, допущенное к управлению, для изменения
     */
    private void showDialogChange(PersonAllowedToDrive personAllowedToDrive) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/personAllowedToDrive/changePersonAllowedToDrive.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Изменение лица, допущенного к управлению");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            ChangePersonAllowedToDriveController controller = loader.getController();
            controller.setAddStage(addStage, personAllowedToDrive.getId());
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