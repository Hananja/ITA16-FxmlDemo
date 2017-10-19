package de.szut.ita16.javafx.helloWorldExtended;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class Controller {
    @FXML
    private TextField txtName;
    @FXML
    private Button btnHello;

    public void handleBtnHelloClicked(ActionEvent actionEvent) {
        final String name = txtName.getText();
        if( name.length() > 0 ) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Hello %s!", name));
            alert.show();
        }
        log.info(actionEvent.toString());
    }

    public void handleTxtNameKey(KeyEvent keyEvent) {
        // Button only enabled if text is entered
        btnHello.setDisable(txtName.getText().length() == 0);

        log.info( "Key: " + keyEvent.getText() );
    }
}
