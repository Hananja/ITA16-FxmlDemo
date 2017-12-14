package de.szut.ita16.javafx.controls.listviewdbu;

import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implements SQlite DB handling */
@Log
public class Database {
    private Connection connection;
    public Database() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:main.sqlite");

            // create table if not exists
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS items (" +
                        " ID INTEGER PRIMARY KEY, value VARCHAR(20));");
            }
        } catch (SQLException e) {
            log.severe( "Error while connecting DB" + e.toString());
        }
    }

    /** Returns items from database.
     *
     * Note: This method does not return an observable list to guarantee
     * a simple and generic interface and avoid cyclic dependencies.
     *
     * @return List of items or null on error
     */
    public List<Item> getItems() {
        try {
            try (Statement statement = connection.createStatement()) {
                ResultSet result;
                result = statement.executeQuery("SELECT id, value FROM items;");
                List<Item> returnval = new ArrayList<>(result.getFetchSize());
                while (result.next()) {
                    returnval.add(new Item(
                            result.getInt(1) /* primary key */,
                            result.getString(2) /* value */ ));
                }
                return returnval;
            }
        }
        catch (SQLException e) {
            log.severe("SQL Query error " + e);
        }
        return null;
    }

    /** Add item to datababase
     * @param item new item to add
     */
    public Item addItem(Item item ) {
        if( item.getId() != null ) { // item is not new
            throw new IllegalArgumentException("Item has primary key (not new).");
        }

        // get new primaty key
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT MAX(ID) + 1 AS newid FROM items;");
            int newID = result.getInt(1);
            item.setId(newID);

            try (PreparedStatement insertstatement = connection.prepareStatement(
                    "INSERT INTO items (id, value) VALUES (?,?);")) {
                insertstatement.setInt(1, item.getId());
                insertstatement.setString(2, item.getValue());
                insertstatement.execute();

                return item;
            } catch (SQLException e) {
                log.severe("SQL Insert error " + e);
            }
        } catch (SQLException e) {
            log.severe("SQL Primary Key Discovery error " + e);
        }
        return null;
    }

    /** Remove an element from databas
     *
     * @param item item to delete
     * @return number of deleted items or -1 on error
     *
     * FIXME: ensure deletion of only one item according to primary key
     */
    public int deleteItem( Item item ) {
        if( item.getId() == null ) {
            throw new IllegalArgumentException("no existing item");
        }

        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id == ?;")) {
            statement.setInt(1, item.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.severe("SQL Delete error " + e);
        }
        return -1;
    }

    // TODO: implement update during winter holydays

    public static void main(String args[]) {
        Database db = new Database();
        db.addItem(new Item("Test 1"));
        db.addItem(new Item("Test 2"));
        db.addItem(new Item("Test 3"));

        for( Item item: db.getItems()) {
            System.out.println(item);
        }

        System.out.println("---");

        db.deleteItem(db.getItems().get(0)); // delete first one

        for( Item item: db.getItems()) {
            System.out.println(item);
        }
    }
}
