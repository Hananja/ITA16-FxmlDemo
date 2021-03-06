package de.szut.ita16.javafx.helloWorldExtended;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.java.Log;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(
                getClass().getResource("hello.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(
                new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Logger controllerLogger = Logger.getLogger(Controller.class.getName());
        controllerLogger.setLevel(Level.WARNING);

        launch(args);
    }
}
