package de.szut.ita16.javafx.controls.listviewdbu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // FMXLLoader Objekt erzeugen, damit hinterher ein
        // Zugriff auf den vom Loader erzeugten Controller möglich ist
        FXMLLoader loader = new FXMLLoader();

        // .openStream() hinter dem getResource(), damit die nicht statische
        // .load() Methode auf dem loader-Objekt aufgerufen wird
        Parent root = loader.load(
                getClass().getResource("gui.fxml").openStream());

        // Controller aus dem Loader auslesen, um darauf die init()-
        // Methode aufrufen zu können (GUI ist fertig, aber unsichtbar)
        Controller controller = loader.getController();
        controller.init(); // initialisiert die GUI

        primaryStage.setTitle("ListView Demo");
        primaryStage.setScene(
                new Scene(root, 300, 275));
        primaryStage.setMinHeight(120);
        primaryStage.setMinWidth(200);
        primaryStage.show(); // Anzeigen
    }


    public static void main(String[] args) {
        launch(args);
    }
}
