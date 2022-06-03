package com.insuranceagency.controllerPolicy;

import com.insuranceagency.Main;
import com.insuranceagency.controllerCar.AddCarController;
import com.insuranceagency.controllerCar.AllCarsController;
import com.insuranceagency.controllerPersonAllowedToDrive.AddPersonAllowedToDriveController;
import com.insuranceagency.controllerPolicyholder.AddPolicyholderController;
import com.insuranceagency.controllerPolicyholder.AllPolicyholdersController;
import com.insuranceagency.database.DBPolicy;
import com.insuranceagency.database.Database;
import com.insuranceagency.model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Класс Контроллер для представления <b>addPolicy.fxml</b>.
 * <p>Данный класс предназначен для добавления полиса.</p>
 */
public class AddPolicyController {
    @FXML
    private ComboBox<String> cbInsuranceType;
    @FXML
    private TextField tfInsurancePremium;
    @FXML
    private TextField tfInsuranceAmount;
    @FXML
    private DatePicker dpDateOfConclusion;
    @FXML
    private ComboBox<String> cbExpirationDate;
    @FXML
    private TextField tfPolicyholder;
    @FXML
    private TextField tfCar;
    @FXML
    private TextField tfEmployee;

    @FXML
    private TextField tfCountPersonsAllowedToDrive;

    private Policyholder selectedPolicyholder;
    private Car selectedCar;
    private Employee selectedEmployee;
    private ArrayList<PersonAllowedToDrive> listPersonAllowedToDrive;

    @FXML
    void initialize() {
        ObservableList<String> insuranceType = FXCollections.observableArrayList("ОСАГО", "КАСКО");
        cbInsuranceType.setItems(insuranceType);

        ObservableList<String> expirationDate = FXCollections.observableArrayList("6 месяцев", "12 месяцев");
        cbExpirationDate.setItems(expirationDate);

        selectedEmployee = Database.getUser();
        tfEmployee.setText(selectedEmployee.getFullName());

        dpDateOfConclusion.setValue(LocalDate.now());

        listPersonAllowedToDrive = new ArrayList<>();
        tfCountPersonsAllowedToDrive.setText("0");
    }


    /**
     * По нажатию на кнопку "+" около страхователя вызывает метод показа сцены добавление страхователя
     * и устанавливает добавленного страхователя в tfPolicyholder
     */
    public void onAddPolicyholder(ActionEvent actionEvent) {
        showDialogAddPolicyholder();
        if(selectedPolicyholder != null) {
            tfPolicyholder.setText(selectedPolicyholder.getFullName());
        }
    }
    /**
     * Показ сцены добавления страхователя
     */
    private void showDialogAddPolicyholder() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policyholder/addPolicyholder.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Добавление страхователя");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            AddPolicyholderController controller = loader.getController();
            controller.setAddStage(addStage);
            addStage.showAndWait();
            Policyholder policyholder = controller.getPolicyholderForPolicy();
            if(policyholder != null) selectedPolicyholder = policyholder;
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }


    /**
     * По нажатию на кнопку "Выбрать" около страхователя вызывает метод показа сцены всего списка страхователей
     * и устанавливает выюранного страхователя в tfPolicyholder
     */
    public void onChoosePolicyholder(ActionEvent actionEvent) {
        showDialogChoosePolicyholder();
        if(selectedPolicyholder != null) {
            tfPolicyholder.setText(selectedPolicyholder.getFullName());
        }
    }
    /**
     * Показ сцены всего списка страхователей
     */
    private void showDialogChoosePolicyholder() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/policyholder/allPolicyholders.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Выбор страхователей");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            AllPolicyholdersController controller = loader.getController();
            controller.setAddStage(addStage);
            addStage.showAndWait();
            Policyholder policyholder = controller.getPolicyholderForPolicy();
            if(policyholder != null) selectedPolicyholder = policyholder;
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }


    /**
     * По нажатию на кнопку "+" около автомобиля вызывает метод показа сцены добавление автомобиля
     * и устанавливает добавленный автомобиль в tfCar
     */
    public void onAddCar(ActionEvent actionEvent) {
        showDialogAddCar();
        if(selectedCar != null) {
            tfCar.setText(selectedCar.getModel());
        }
    }
    /**
     * Показ сцены добавления автомобиля
     */
    private void showDialogAddCar() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/car/addCar.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Добавление автомобиля");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            AddCarController controller = loader.getController();
            controller.setAddStage(addStage);
            addStage.showAndWait();
            Car car = controller.getCarForPolicy();
            if(car != null) selectedCar = car;
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }


    /**
     * По нажатию на кнопку "Выбрать" около автомобиля вызывает метод показа сцены всего списка автомобилей
     * и устанавливает выбранный автомобиль в tfCar
     */
    public void onChooseCar(ActionEvent actionEvent) {
        showDialogChooseCar();
        if(selectedCar != null) {
            tfCar.setText(selectedCar.getModel());
        }
    }
    /**
     * Показ сцены всего списка автомобилей
     */
    private void showDialogChooseCar() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/car/allCars.fxml"));
            Parent page = loader.load();
            Stage addStage = new Stage();
            addStage.setTitle("Выбор автомобиля");
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            addStage.setScene(scene);
            AllCarsController controller = loader.getController();
            controller.setAddStage(addStage);
            addStage.showAndWait();
            Car car = controller.getCarForPolicy();
            if(car != null) selectedCar = car;
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
     * Считывание данных с формы и проверка правильности введённых данных
     * @return Сформированный страхователь
     */
    private Policy readDate() throws Exception {
        String insuranceType = cbInsuranceType.getValue();
        if (insuranceType == null)
        {
            throw new Exception("Заполните поле Вид страхования");
        }

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

        String insuranceAmountTemp = tfInsuranceAmount.getText().trim();
        if (insuranceAmountTemp.isEmpty())
        {
            throw new Exception("Заполните поле Страховая сумма");
        }
        int insuranceAmount;
        try { insuranceAmount = Integer.parseInt(insuranceAmountTemp); }
        catch (Exception exp) { throw new Exception("Страховая сумма должна быть целым числом"); }

        if(insuranceAmount <= insurancePremium){
            throw new Exception("Страховая сумма должна быть больше Страховой премии");
        }

        LocalDate dateOfConclusion = dpDateOfConclusion.getValue();

        String expirationDateTemp = cbExpirationDate.getValue();
        LocalDate expirationDate = dateOfConclusion;
        if (expirationDateTemp == null)
        {
            throw new Exception("Заполните поле Срок действия");
        }
        else if (expirationDateTemp.equals("6 месяцев"))
        {
            expirationDate = expirationDate.plusMonths(6);
        }
        else
        {
            expirationDate = expirationDate.plusYears(1);
        }

        return new Policy(insuranceType, insurancePremium, insuranceAmount, dateOfConclusion, expirationDate, selectedPolicyholder.getId(), selectedCar.getId(), selectedEmployee.getId());
    }
    /**
     * Вызов метода добавления полиса в БД по нажатию на кнопку "Добавить"
     */
    public void onAdd(ActionEvent actionEvent) {
        try {
            if (selectedPolicyholder == null) throw new Exception("Выберите страхователя");
            if (selectedCar == null) throw new Exception("Выберите автомобиль");
            if (selectedEmployee == null) throw new Exception("Выберите сотрудника");
            if (listPersonAllowedToDrive.size() == 0) throw new Exception("Список лиц, допущенных к управлению пуст");

            Policy policy = readDate();
            DBPolicy.addPolicyWithConnections(policy, listPersonAllowedToDrive);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Полис успешно добавлен");
            alert.showAndWait();

            clear();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Очищает поля на форме
     */
    private void clear(){
        selectedPolicyholder = null;
        selectedCar = null;
        listPersonAllowedToDrive.clear();

        cbInsuranceType.valueProperty().setValue(null);
        tfInsurancePremium.clear();
        tfInsuranceAmount.clear();
        dpDateOfConclusion.setValue(LocalDate.now());
        cbExpirationDate.valueProperty().setValue(null);
        tfPolicyholder.clear();
        tfCar.clear();
        tfCountPersonsAllowedToDrive.setText("0");
    }
}
