package com.insuranceagency.controllerPolicy;

import com.insuranceagency.Main;
import com.insuranceagency.database.DBInsuranceEvent;
import com.insuranceagency.database.DBPolicy;
import com.insuranceagency.model.InsuranceEvent;
import com.insuranceagency.model.Policy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Класс Контроллер для представления <b>addInsuranceEvent.fxml</b>.
 * <p>Данный класс предназначен для добавления страхового случая полису.</p>
 */
public class AddInsuranceEventController {
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextField tfInsurancePayment;
    @FXML
    private TextArea taDescription;

    private Policy policy;

    @FXML
    void initialize() {
        dpDate.setValue(LocalDate.now());
    }

    /**
     * Метод для передачи данных при загрузке сцены
     * @param policyId Id полиса для закрепления страхового случая
     */
    public void setAddStage(int policyId) {
        try {
            policy = DBPolicy.searchPolicyID(policyId);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     * @return Сформированный страховой случай
     */
    private InsuranceEvent readDate() throws Exception {

        LocalDate date = dpDate.getValue();
        if (date.isBefore(policy.getDateOfConclusion()))
        {
            throw new Exception("Дата не может быть меньше даты заключения полиса");
        }
        if (date.isAfter(policy.getExpirationDate()))
        {
            throw new Exception("Дата не может быть больше даты окончания действия полиса");
        }

        String insurancePaymentTemp = tfInsurancePayment.getText().trim();
        if (insurancePaymentTemp.isEmpty())
        {
            throw new Exception("Заполните поле Страховая выплата");
        }
        int insurancePayment;
        try { insurancePayment = Integer.parseInt(insurancePaymentTemp); }
        catch (Exception exp) { throw new Exception("Страховая выплата должна быть целым числом"); }
        if (policy.getInsuranceType().equals("КАСКО") && insurancePayment > policy.getInsuranceAmount())
        {
            throw new Exception("Страховая выплата должна быть меньше Страховой суммы");
        }

        String description = taDescription.getText().trim();
        if(description.isEmpty()){
            throw new Exception("Заполните поле Описание");
        }

        return new InsuranceEvent(date, insurancePayment, description, policy.getId());
    }

    /**
     * Вызов метода добавления страхового случая в БД по нажатию на кнопку "Добавить"
     */
    public void onAdd(ActionEvent actionEvent) {
        try {
            InsuranceEvent insuranceEvent = readDate();
            DBInsuranceEvent.addInsuranceEvent(insuranceEvent);

            showDialogDetail(policy);
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
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
            addStage.show();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }
}
