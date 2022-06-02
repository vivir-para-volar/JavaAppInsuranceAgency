package com.insuranceagency.controllerPolicy;

import com.insuranceagency.Main;
import com.insuranceagency.MainController;
import com.insuranceagency.controllerPersonAllowedToDrive.AddPersonAllowedToDriveController;
import com.insuranceagency.database.DBInsuranceEvent;
import com.insuranceagency.database.DBPersonAllowedToDrive;
import com.insuranceagency.database.DBPolicy;
import com.insuranceagency.model.PersonAllowedToDrive;
import com.insuranceagency.model.Policy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChangePolicyController {
    @FXML
    private TextField tfInsuranceType;
    @FXML
    private TextField tfInsurancePremium;
    @FXML
    private TextField tfInsuranceAmount;
    @FXML
    private TextField tfDateOfConclusion;
    @FXML
    private DatePicker dpExpirationDate;
    @FXML
    private TextField tfPolicyholder;
    @FXML
    private TextField tfCar;
    @FXML
    private TextField tfEmployee;
    @FXML
    private TextField tfCountPersonsAllowedToDrive;

    private Policy policy;
    private ArrayList<PersonAllowedToDrive> listPersonAllowedToDrive;

    @FXML
    void initialize() {
        listPersonAllowedToDrive = new ArrayList<>();
        tfCountPersonsAllowedToDrive.setText("0");
    }

    private boolean fromChange;
    public void setAddStage(boolean fromChange, int policyId) {
        this.fromChange = fromChange;

        try {
            policy = DBPolicy.searchPolicyID(policyId);
            if(policy != null) {
                policy.searchName();
                fillInfo();
            }
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Заполняет поля информацией о выбранном полисе
     */
    private void fillInfo(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        tfInsuranceType.setText(policy.getInsuranceType());
        tfInsurancePremium.setText(String.valueOf(policy.getInsurancePremium()));
        tfInsuranceAmount.setText(String.valueOf(policy.getInsuranceAmount()));
        tfDateOfConclusion.setText(policy.getDateOfConclusion().format(dtf));
        dpExpirationDate.setValue(policy.getExpirationDate());
        tfPolicyholder.setText(policy.getPolicyholderName());
        tfCar.setText(policy.getCarModel());
        tfEmployee.setText(policy.getEmployeeName());

        try {
            listPersonAllowedToDrive = DBPersonAllowedToDrive.searchPersonsAllowedToDrivePolicyId(policy.getId());
            tfCountPersonsAllowedToDrive.setText(String.valueOf(listPersonAllowedToDrive.size()));
        }
        catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     */
    private void readDate() throws Exception {
        String insurancePremiumTemp = tfInsurancePremium.getText().trim();
        if (insurancePremiumTemp.isEmpty())
        {
            throw new Exception("Заполните поле Страховая премия");
        }
        int insurancePremium;
        try { insurancePremium = Integer.parseInt(insurancePremiumTemp); }
        catch (Exception exp) { throw new Exception("Страховая премия должна быть целым числом"); }
        if(insurancePremium <= 0){
            throw new Exception("Страховая премия должна быть больше 0");
        }

        LocalDate expirationDate = dpExpirationDate.getValue();
        if(expirationDate.isBefore(policy.getDateOfConclusion()))
        {
            throw new Exception("Дата окончания действия не может быть меньше даты заключения");
        }
        if(expirationDate.isAfter(policy.getExpirationDate()))
        {
            throw new Exception("Срок действия полиса нельзя увеличить");
        }
        LocalDate maxDate = DBInsuranceEvent.searchInsuranceEventMaxDate(policy.getId());
        if (maxDate != null)
        {
            if(expirationDate.isBefore(maxDate))
            {
                throw new Exception("Дата окончания действия не может быть меньше даты последнего страхового случая");
            }
        }

        policy = new Policy(policy.getId(), policy.getInsuranceType(), insurancePremium, policy.getInsuranceAmount(), policy.getDateOfConclusion(), expirationDate, policy.getPolicyholderId(), policy.getCarId(), policy.getEmployeeId());
    }
    /**
     * Изменение полиса
     */
    public void onChange(ActionEvent actionEvent) {
        try {
            if (listPersonAllowedToDrive.size() == 0) throw new Exception("Список лиц, допущенных к управлению пуст");

            readDate();
            DBPolicy.changePolicyWithConnections(policy, listPersonAllowedToDrive);
            showDialogDetail(policy);
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
     * По нажатию на кнопку "+" около около лиц, допущенных к управлению, вызывает метод показа сцены добавления
     * и устанавливает количество лиц, допущенных к управлению, в tfCountPersonsAllowedToDrive
     */
    public void onAddPersonAllowedToDrive(ActionEvent actionEvent) {
        showDialogAddPersonAllowedToDrive();
        int count = listPersonAllowedToDrive.size();
        tfCountPersonsAllowedToDrive.setText(String.valueOf(count));
    }
    /**
     * Показ сцены добавления лица, допущенного к управлению
     */
    private void showDialogAddPersonAllowedToDrive() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/personAllowedToDrive/addPersonAllowedToDrive.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Добавление лица, допущенного к управлению");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            AddPersonAllowedToDriveController controller = loader.getController();
            controller.setAddStage(addStage);
            addStage.showAndWait();
            PersonAllowedToDrive selectedPersonAllowedToDrive = controller.getPersonAllowedToDriveForPolicy();
            if(selectedPersonAllowedToDrive != null) {
                listPersonAllowedToDrive.add(selectedPersonAllowedToDrive);
            }
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }
    
    /**
     * По нажатию на кнопку "Выбрать" около лиц, допущенных к управлению, вызывает метод показа сцены изменения списка
     * и устанавливает количество лиц, допущенных к управлению, в tfCountPersonsAllowedToDrive
     */
    public void onChoosePersonsAllowedToDrive(ActionEvent actionEvent) {
        showDialogChoosePersonsAllowedToDrive();
        int count = listPersonAllowedToDrive.size();
        tfCountPersonsAllowedToDrive.setText(String.valueOf(count));
    }
    /**
     * Показ сцены изменения списка, лиц допущенных к управлению
     */
    private void showDialogChoosePersonsAllowedToDrive() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policy/choosePersonsAllowedToDrive.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Изменения списка, лиц допущенных к управлению");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            ChoosePersonsAllowedToDriveController controller = loader.getController();
            controller.setAddStage(addStage, listPersonAllowedToDrive);
            addStage.showAndWait();
            listPersonAllowedToDrive = controller.getListPersonAllowedToDrive();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }

    /**
     * Возврат к сцене выбора полиса у страхователя или списку всех полисов
     */
    public void onCancel(ActionEvent actionEvent) {
        if(fromChange) showDialogCancelFromChange(policy);
        else showDialogCancel();
    }
    /**
     * Показ сцены выбора полиса у страхователя
     * @param policy Полис
     */
    private void showDialogCancelFromChange(Policy policy) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("view/policy/policy.fxml"));
            Parent page = loader.load();
            MainController.getBorderPane.setCenter(page);
            PolicyController controller = loader.getController();
            controller.setAddStage(policy.getPolicyholderId());
        } catch (IOException exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }
    /**
     * Показ сцены списка всех полисов
     */
    private void showDialogCancel() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("view/policy/allPolicies.fxml"));
            Parent page = loader.load();
            MainController.getBorderPane.setCenter(page);
        } catch (IOException exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }

    /**
     * Вызывает сцену добавления страхового случая при нажатии на кнопку "Добавить страховой случай"
     */
    public void onAddInsuranceEvent(ActionEvent actionEvent) {
        showDialogAddInsuranceEvent(policy);
    }
    private void showDialogAddInsuranceEvent(Policy policy) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policy/addInsuranceEvent.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Добавление лица, допущенного к управлению");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            AddInsuranceEventController controller = loader.getController();
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
}