package com.insuranceagency;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Класс запуска приложения
 */
public class Main extends Application {
    private static Stage primaryStage;
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Загрузка первой сцены
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Страховое агентство");
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Метод для запуска приложения
     */
    public static void main(String[] args) {
        launch();
    }
}