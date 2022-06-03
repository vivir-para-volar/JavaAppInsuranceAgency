package com.insuranceagency.controllerReport;

import com.insuranceagency.database.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Класс Контроллер для представления <b>report.fxml</b>.
 * <p>Данный класс предназначен для формирования отчётов.</p>
 */
public class ReportController {
    @FXML
    private ComboBox<String> cbInsuranceType;
    @FXML
    private DatePicker dpDateStart;
    @FXML
    private DatePicker dpDateEnd;

    @FXML
    private TextField tfCountContracts;
    @FXML
    private TextField tfSumContracts;
    @FXML
    private TextField tfSumInsuranceEvents;

    @FXML
    void initialize() {
        ObservableList<String> insuranceType = FXCollections.observableArrayList("ОСАГО и КАСКО", "ОСАГО", "КАСКО");
        cbInsuranceType.setItems(insuranceType);

        dpDateEnd.setValue(LocalDate.now());
        dpDateStart.setValue(LocalDate.now().minusYears(1));
    }

    /**
     * Формирует финансовый отчёт при нажатии на кнопку "Сформировать"
     */
    public void onCreateReport(ActionEvent actionEvent){
        try {
            String insuranceType = cbInsuranceType.getValue();
            if (insuranceType == null)
            {
                throw new Exception("Заполните поле Вид страхования");
            }

            LocalDate dateStart = dpDateStart.getValue();
            LocalDate dateEnd = dpDateEnd.getValue();

            if (dateStart.isAfter(dateEnd))
            {
                throw new Exception("Дата начала не может быть больше даты окончания");
            }


            ArrayList<Integer> list = Database.createReport(insuranceType, dateStart, dateEnd);

            tfCountContracts.setText(list.get(0).toString());
            tfSumContracts.setText(list.get(1).toString());
            tfSumInsuranceEvents.setText(list.get(2).toString());
        }
        catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }
}
