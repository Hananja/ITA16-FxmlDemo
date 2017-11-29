package de.szut.ita16.javafx.controls.listviewdbu;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class Controller {
    @FXML
    private ListView<String> listMain;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnRemove;

    ObservableList<String> model;

    public void init() {
        // set data model
        model = FXCollections.observableArrayList("Test 1", "Test 2", "Test 3");
        listMain.setItems(model);

        // initial button state
        updateBtnRemove();

        // selection change should update button state
        listMain.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<String>) c -> updateBtnRemove());
    }

    /** enable or disable button for remove according to selection */
    public void updateBtnRemove() {
        btnRemove.setDisable(!(listMain
                .getSelectionModel().getSelectedItems().size() > 0));
    }

    public void onBtnInsert(ActionEvent actionEvent) {
        // TODO: implement insert
    }

    public void onBtnRemove(ActionEvent actionEvent) {
        // FIXME: implement multi selections
        model.remove(listMain.getSelectionModel().getSelectedIndex());
    }

}
