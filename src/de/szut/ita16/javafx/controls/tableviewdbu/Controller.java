package de.szut.ita16.javafx.controls.tableviewdbu;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;

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

        TableColumn<Item,String> valueCol = new TableColumn<>("Wert");
        valueCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Item, String>,
                        ObservableValue<String>>() {
            public ObservableValue<String> call(
                    TableColumn.CellDataFeatures<Item, String> param) {
                // param.getValue() returns the Person instance for a
                // particular TableView row
                return new ReadOnlyObjectWrapper<>(param.getValue().
                        getValue());
            }
        });

        TableColumn<Item,Integer> countCol = new TableColumn<>("Anzahl");
        // alternative use of lambda notation
        countCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(
                        param.getValue().getCount()));

        tableMain.getColumns().setAll(valueCol, countCol);

    }

    /** enable or disable buttons for remove according to selection */
    public void updateBtns() {
        btnRemove.setDisable(tableMain
                .getSelectionModel().getSelectedItems().size() <= 0);
        btnEdit.setDisable(tableMain
                .getSelectionModel().getSelectedItems().size() != 1);
    }

    /** shows edit dialog for new and for existing items
     *
     * @param contentText content text of dialog
     * @param title title of dialog
     * @param headerText header text of dialog
     * @param item item to edit or null to create a new one
     * @return reference to modified or created item
     */
    public Optional<Item> showEditDialog(String contentText,
                                         String title,
                                         String headerText,
                                         Item item ) {
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        dialog.setHeaderText(headerText);

        // Buttons
        dialog.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        // Input Fields with GridPane Layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(5,5,5,5));

        Label valueLabel = new Label("Wert:");

        TextField textField = new TextField(
                item == null ? "" : item.getValue());
        textField.setPromptText("Wert");

        Label countLabel = new Label("Anzahl:");

        Spinner<Integer> spinner =
                new Spinner<>(0, Integer.MAX_VALUE,
                        item == null ? 0 : item.getCount());

        // first: colIndex, second: rowIndex
        gridPane.add( valueLabel, 0, 0);
        gridPane.add( textField,1,0);
        gridPane.add( countLabel, 0, 1);
        gridPane.add( spinner, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on text input field as defalut
        Platform.runLater(() -> textField.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(textField.getText(), spinner.getValue());
            }
            return null;
        });

        Optional<Pair<String, Integer>> result = dialog.showAndWait();

        if(result.isPresent()) { // OK
            if(item == null) {
                // new item necessary
                item = new Item(
                        result.get().getKey() /* first val of pair */,
                        result.get().getValue() /* second val of pair */);
            } else {
                // update existing item
                item.setValue(result.get().getKey());
                item.setCount(result.get().getValue());
            }
            return Optional.of(item);
        } else { // cancel
            return Optional.empty();
        }
    }

    public void onBtnInsert() {
        Optional<Item> result =
                showEditDialog("neuer Eintrag", "Hinzufügen",
                "Hier kann ein neuer Eintrag hinzugefügt werden",
                        null);
        if( result.isPresent() ) {
            Item item = database.addItem(result.get()); // get Item with new id
            model.add(item);
        }
    }

    public void onBtnRemove() {
        // FIXME: implement multi selections

        // important: delete from database first
        database.deleteItem(tableMain.getSelectionModel().getSelectedItem());
        model.remove(tableMain.getSelectionModel().getSelectedIndex());
    }

    public void onBtnEdit() {
        Optional<Item> result = showEditDialog("Wert editieren:",
                "Wert anpassen",
                "Hier kann der Wert eines Eintrags bearbeitet werden",
                tableMain.getSelectionModel().getSelectedItem());
        if( result.isPresent() ) {
            // workaround: remove old item and add new one, because item is not observable
            // (alternative: warp item with properties and use extractor:
            // see https://docs.oracle.com/javase/8/javafx/api/javafx/collections/FXCollections.html#observableArrayList-javafx.util.Callback-)
            model.remove(result.get());
            database.updateItem(result.get());
            // removing and adding immediately again does not work
            // workaround: add after list update
            Platform.runLater(() -> model.add(result.get()));
        }
    }
}
