package de.szut.ita16.javafx.controls.tableviewdbu;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.util.Callback;

import java.util.Optional;

public class Controller {
    @FXML
    private Button btnEdit;
    @FXML
    private TableView<Item> tableMain;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnRemove;

    private ObservableList<Item> model;
    private Database database;

    public void init() {
        // set data model
        database = new Database();
        model = FXCollections.observableArrayList( database.getItems() );

        tableMain.setItems(model);

        // initial button state
        updateBtns();

        // selection change should update button states
        tableMain.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<Item>)
                        c -> Controller.this.updateBtns());

        TableColumn<Item,String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                // param.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper<>(param.getValue().getValue());
            }
        });

        TableColumn<Item,Integer> countCol = new TableColumn<>("Count");
        // alternative use of lambda notation
        countCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getCount()));

        tableMain.getColumns().setAll(valueCol, countCol);

    }

    /** enable or disable buttons for remove according to selection */
    public void updateBtns() {
        btnRemove.setDisable(tableMain
                .getSelectionModel().getSelectedItems().size() <= 0);
        btnEdit.setDisable(tableMain
                .getSelectionModel().getSelectedItems().size() != 1);
    }

    public void onBtnInsert(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("New Item");
        dialog.setContentText("neuer Eintrag:");
        dialog.setTitle("Hinzufügen");
        dialog.setHeaderText("Hier kann ein neuer Eintrag hinzugefügt werden");
        Optional<String> result = dialog.showAndWait(); // wait for user input
        if( result.isPresent() ) {
            Item item = new Item(result.get());
            item = database.addItem(item); // get Item with new id
            model.add(item);
        }
    }

    public void onBtnRemove(ActionEvent actionEvent) {
        // FIXME: implement multi selections

        // important: delete from database first
        database.deleteItem(tableMain.getSelectionModel().getSelectedItem());
        model.remove(tableMain.getSelectionModel().getSelectedIndex());
    }

    public void onBtnEdit() {
        Item item = tableMain.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog(item.getValue());
        dialog.setTitle("Wert anpassen");
        dialog.setContentText("neuer Wert:");
        dialog.setHeaderText("Hier kann der Wert eines Eintrags bearbeitet werden");
        Optional<String> newvalue = dialog.showAndWait(); // wait for user input
        if( newvalue.isPresent() ) {
            // workaround: remove old item and add new one, because item is not observable
            // (alternative: warp item with properties and use extractor:
            // see https://docs.oracle.com/javase/8/javafx/api/javafx/collections/FXCollections.html#observableArrayList-javafx.util.Callback-)
            model.remove(item);
            item.setValue(newvalue.get());
            database.updateItem(item);
            // removing and adding immediately again does not work
            // workaround: add after list update
            Platform.runLater(() -> model.add(item));
        }
    }
}
