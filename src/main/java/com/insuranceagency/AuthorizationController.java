package com.insuranceagency;

import com.insuranceagency.database.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Класс Контроллер для представления <b>authorization.fxml</b>.
 * <p>Данный класс предназначен для авторизации пользователя.</p>
 */
public class AuthorizationController {
    @FXML
    private TextField tfLogin;
    @FXML
    private TextField pfPassword;

    /**
     * При нажатии на кнопку "Войти" вызывает метод авторизации
     */
    public void onAuthorization(ActionEvent actionEvent) {
        String login = tfLogin.getText().trim();
        String password = pfPassword.getText().trim();

        try {
            Database.authorization(login, password);
            showDialogMain(actionEvent);
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exp.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Показ основной сцены
     */
    private void showDialogMain(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Страховое агентство");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(scene);
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (Exception exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось загрузить страницу");
            alert.showAndWait();
        }
    }
}