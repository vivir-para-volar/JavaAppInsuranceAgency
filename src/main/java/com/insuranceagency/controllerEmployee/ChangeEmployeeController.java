package com.insuranceagency.controllerEmployee;

import com.insuranceagency.database.DBEmployee;
import com.insuranceagency.model.Employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

/**
 * Класс Контроллер для представления <b>changeEmployee.fxml</b>.
 * <p>Данный класс предназначен для изменения сотрудника.</p>
 */
public class ChangeEmployeeController {
    @FXML
    private TextField tfSearch;
    @FXML
    private  TextField tfFullName;
    @FXML
    private  TextField tfBirthday;
    @FXML
    private  TextField tfTelephone;
    @FXML
    private  TextField tfPassport;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField pfPassword;

    private Employee employee;

    private Stage dialogStage;
    /**
     * Метод для передачи данных при загрузке сцены
     * @param addStage Текущая сцена
     * @param employeeId Id сотрудника
     */
    public void setAddStage(Stage addStage, int employeeId) {
        this.dialogStage = addStage;

        try {
            employee = DBEmployee.searchEmployeeID(employeeId);
            fillInfo();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Заполняет поля информацией о выбранном сотруднике
     */
    private void fillInfo(){
        tfFullName.setText(employee.getFullName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        tfBirthday.setText(employee.getBirthday().format(formatter));

        tfTelephone.setText(employee.getTelephone());
        tfPassport.setText(employee.getPassport());
        tfLogin.setText(employee.getLogin());
    }

    /**
     * Поиск сотрудника в БД по номеру телефона или паспорту при нажатии на кнопку "Поиск"
     */
    public void onSearch(ActionEvent actionEvent) {
        try {
            String search = tfSearch.getText();
            employee = DBEmployee.searchEmployeeTelephoneOrPassport(search);
            fillInfo();
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
     * Считывание данных с формы и проверка правильности введённых данных
     */
    private void readDate() throws Exception {
        String fullName = tfFullName.getText().trim();
        if (fullName.isEmpty()) {
            throw new Exception("Заполните поле ФИО");
        }

        String telephone = tfTelephone.getText().trim();
        if (telephone.isEmpty()) {
            throw new Exception("Заполните поле Номер телефона");
        }
        if (telephone.length() > 15) {
            throw new Exception("Номер телефона не может быть больше 15 символов");
        }
        for (var i = 0; i < telephone.length(); i++) {
            if (!Character.isDigit(telephone.charAt(i))) {
                throw new Exception("Номер телефона должен содержать только цифры");
            }
        }

        String passport = tfPassport.getText().trim();
        if (passport.isEmpty()) {
            throw new Exception("Заполните поле Паспорт");
        }
        if (passport.length() != 10) {
            throw new Exception("Паспорт должен содержать 10 цифр");
        }
        for (var i = 0; i < passport.length(); i++) {
            if (!Character.isDigit(passport.charAt(i))) {
                throw new Exception("Паспорт должен содержать только цифры");
            }
        }

        String login = tfLogin.getText().trim();
        if (login.isEmpty()) {
            throw new Exception("Заполните поле Логин");
        }
        if (login.length() < 4 || login.length() > 32)
        {
            throw new Exception("Длина логина должна быть от 4 до 32 символов");
        }
        for (var i = 0; i < login.length(); i++) {
            if (Character.isWhitespace(login.charAt(i))) {
                throw new Exception("Логин не может содержать пробелы");
            }
        }

        String password = pfPassword.getText().trim();
        if (password.isEmpty()) {
            password = employee.getPassword();
        }
        else {
            if (password.length() < 4 || password.length() > 32) {
                throw new Exception("Длина пароля должна быть от 4 до 32 символов");
            }
            for (var i = 0; i < login.length(); i++) {
                if (Character.isWhitespace(password.charAt(i))) {
                    throw new Exception("Пароль не может содержать пробелы");
                }
            }
        }

        employee = new Employee(employee.getId(), fullName, employee.getBirthday(), telephone, passport, login, password);
    }

    /**
     * Вызов метода изменения сотрудника в БД по нажатию на кнопку "Изменить"
     */
    public void onChange(ActionEvent actionEvent) {
        try {
            if (employee == null) throw new Exception("Сотрудник не выбран");

            readDate();
            boolean changePassword = !pfPassword.getText().trim().isEmpty();
            DBEmployee.changeEmployee(employee, changePassword);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Сотрудник успешно изменён");
            alert.showAndWait();

            if(dialogStage != null ) dialogStage.close();
            else clear();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Вызов метода удаления сотрудника из БД по нажатию на кнопку "Удалить"
     */
    public void onDelete(ActionEvent actionEvent) {
        try {
            if (employee == null) throw new Exception("Сотрудник не выбран");

            DBEmployee.deleteEmployee(employee.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Сотрудник успешно удалён");
            alert.showAndWait();

            if(dialogStage != null ) dialogStage.close();
            else clear();
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
        employee = null;

        tfFullName.clear();
        tfBirthday.clear();
        tfTelephone.clear();
        tfPassport.clear();
        tfLogin.clear();
        pfPassword.clear();
    }
}
