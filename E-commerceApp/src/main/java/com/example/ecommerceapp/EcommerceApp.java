package com.example.ecommerceapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EcommerceApp extends Application {

    private UserInterface ui = new UserInterface();
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(ui.createContent());
        stage.setTitle("Welcome To E-commerce App!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}