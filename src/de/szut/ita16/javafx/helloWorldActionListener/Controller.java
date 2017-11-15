package de.szut.ita16.javafx.helloWorldActionListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField txtName;
    @FXML
    private Button btnHello;

    /** Initialisiert die GUI.
     * Diese Methode ist notwendig, weil im Konstruktor des Controllers
     * die GUI Elemente (btnHello, txtName) noch nicht verfügbar (also
     * noch null) sind. Diese Methode wird nach dem Erzeugen der GUI
     * von der Mail aufgerufen.
     */
    public void init() {
        System.out.println("init");

        // Klassisch: eigene Klasse
        BtnEventHandler btnEventHandler = new BtnEventHandler();
        btnHello.setOnAction(btnEventHandler);

        // Anonyme Klasse:
        btnHello.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Button pressed!");
            }
        });

        // Lambda-Ausdruck (anonyme Methode in einer anonymen Klasse)
        btnHello.setOnAction((event) -> {
            System.out.println("Button pressed!");
        });
    }

    public void handleBtnHelloClicked(ActionEvent actionEvent) {
        final String name = txtName.getText();
        System.out.println(String.format("Hello %s!", name));
    }
}
