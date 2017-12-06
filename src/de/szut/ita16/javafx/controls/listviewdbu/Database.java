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
                        " ID INTEGER PRIMARY KEY AUTOINCREMENT, value VARCHAR(20));");
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
    public List<String> getItems() {
        try {
            try (Statement statement = connection.createStatement()) {
                ResultSet result;
                result = statement.executeQuery("SELECT value FROM items;");
                List<String> returnval = new ArrayList<>(result.getFetchSize());
                while (result.next()) {
                    returnval.add(result.getString(1));
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
     * @param text text of item to add
     */
    public void addItem( String text ) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO items (value) VALUES (?);")) {
            statement.setString(1, text);
            statement.execute();
        } catch (SQLException e) {
            log.severe("SQL Insert error " + e);
        }
    }

    /** Remove an element from databas
     *
     * @param text text of items to delete
     * @return number of deleted items or -1 on error
     *
     * FIXME: ensure deletion of only one item according to primary key
     */
    public int deleteItem( String text ) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE value == ?;")) {
            statement.setString(1, text);
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.severe("SQL Delete error " + e);
        }
        return -1;
    }

    public static void main(String args[]) {
        Database db = new Database();
        db.addItem("Test 1");
        db.addItem("Test 2");
        db.addItem("Test 3");

        for( String item: db.getItems()) {
            System.out.println(item);
        }

        db.deleteItem("Test 1");

        for( String item: db.getItems()) {
            System.out.println(item);
        }
    }
}
