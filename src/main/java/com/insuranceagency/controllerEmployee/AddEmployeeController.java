package com.insuranceagency.controllerEmployee;

import com.insuranceagency.database.DBEmployee;
import com.insuranceagency.model.Employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Класс Контроллер для представления <b>addEmployee.fxml</b>.
 * <p>Данный класс предназначен для добавления сотрудника.</p>
 */
public class AddEmployeeController {
    @FXML
    private TextField tfFullName;
    @FXML
    private DatePicker dpBirthday;
    @FXML
    private TextField tfTelephone;
    @FXML
    private TextField tfPassport;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField pfPassword;

    @FXML
    void initialize() {
        dpBirthday.setValue(LocalDate.EPOCH);
    }

    /**
     * Считывание данных с формы и проверка правильности введённых данных
     * @return Сформированный сотрудник
     */
    private Employee readDate() throws Exception {
        String fullName = tfFullName.getText().trim();
        if (fullName.isEmpty()) {
            throw new Exception("Заполните поле ФИО");
        }

        LocalDate birthday = dpBirthday.getValue();

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
            throw new Exception("Заполните поле Пароль");
        }
        if (password.length() < 4 || password.length() > 32)
        {
            throw new Exception("Длина пароля должна быть от 4 до 32 символов");
        }
        for (var i = 0; i < login.length(); i++) {
            if (Character.isWhitespace(password.charAt(i))) {
                throw new Exception("Пароль не может содержать пробелы");
            }
        }

        return new Employee(fullName, birthday, telephone, passport, login, password);
    }

    /**
     * Вызов метода добавления сотрудника в БД по нажатию на кнопку "Добавить"
     */
    public void onAdd(ActionEvent actionEvent) {
        try
        {
            Employee employee = readDate();
            DBEmployee.addEmployee(employee);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Сотрудник успешно добавлен");
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
        tfFullName.clear();
        dpBirthday.setValue(LocalDate.EPOCH);
        tfTelephone.clear();
        tfPassport.clear();
        tfLogin.clear();
        pfPassword.clear();
    }
}
