package de.szut.ita16.javafx.controls.listviewdbu;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Controller {
    @FXML
    private Button btnEdit;
    @FXML
    private ListView<Item> listMain;
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

        // optional: sort list alphabetically
        // (otherwise add model to listMain directly)
        SortedList<Item> sortedList = new SortedList<Item>(
                model,
                (o1, o2) -> o1.getValue().compareToIgnoreCase(o2.getValue()));

        listMain.setItems(sortedList);

        // initial button state
        updateBtns();

        // selection change should update button states
        listMain.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<Item>)
                        c -> Controller.this.updateBtns());

        listMain.setCellFactory( listView -> new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getValue());
                        }
                    }
                });

    }

    // TODO: implement Update during Winter Holydays

    /** enable or disable buttons for remove according to selection */
    public void updateBtns() {
        btnRemove.setDisable(!(listMain
                .getSelectionModel().getSelectedItems().size() > 0));
        btnEdit.setDisable(!(listMain
                .getSelectionModel().getSelectedItems().size() == 1));
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
        database.deleteItem(listMain.getSelectionModel().getSelectedItem());
        model.remove(listMain.getSelectionModel().getSelectedIndex());
    }

    public void onBtnEdit(ActionEvent actionEvent) {
        Item item = listMain.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog(item.getValue());
        dialog.setTitle("Wert anpassen");
        dialog.setContentText("neuer Wert:");
        dialog.setHeaderText("Hier kann der Wert eines Eintrags bearbeitet werden");
        Optional<String> newvalue = dialog.showAndWait(); // wait for user input
        if( newvalue.isPresent() ) {
            // woraround: remove old item and add new one, because item is not observable
            // (alternative: warp item with properties and use extractor:
            // see https://docs.oracle.com/javase/8/javafx/api/javafx/collections/FXCollections.html#observableArrayList-javafx.util.Callback-)
            model.remove(item);
            item.setValue(newvalue.get());
            database.updateItem(item);
            model.add(item);
        }
    }
}
