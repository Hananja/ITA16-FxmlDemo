package de.szut.ita16.javafx.helloWorldBasic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField txtName;
    @FXML
    private Button btnHello;


    public void handleBtnHelloClicked(ActionEvent actionEvent) {
        final String name = txtName.getText();
        System.out.println(String.format("Hello %s!", name));
    }
}
