package de.szut.ita16.javafx.controls.listviewdbu;

import com.sun.javafx.collections.ObservableSequentialListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Controller {
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

        // FIXME: adapt to Item
        // optional: sort list alphabetically
        // (otherwise add model to listMain directly)
        // SortedList<String> sortedList = new SortedList<String>(
        //        model , String.CASE_INSENSITIVE_ORDER.reversed());

        listMain.setItems(model);

        // initial button state
        updateBtnRemove();

        // selection change should update button state
        listMain.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<Item>) c -> updateBtnRemove());
    }

    /** enable or disable button for remove according to selection */
    public void updateBtnRemove() {
        btnRemove.setDisable(!(listMain
                .getSelectionModel().getSelectedItems().size() > 0));
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
        model.remove(listMain.getSelectionModel().getSelectedIndex());
        database.deleteItem(listMain.getSelectionModel().getSelectedItem());
    }
}
