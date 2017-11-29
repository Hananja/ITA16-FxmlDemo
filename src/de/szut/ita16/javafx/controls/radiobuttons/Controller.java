package de.szut.ita16.javafx.controls.radiobuttons;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

public class Controller {
    @FXML
    private ToggleGroup radioGroup;

    public void init() {
        radioGroup.selectedToggleProperty()
                .addListener((observable, oldValue, newValue) -> {
                    System.out.println("changed from: " + oldValue.toString());
                    System.out.println("changed to: " + newValue.toString());
                });

    }
}
