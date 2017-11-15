package de.szut.ita16.javafx.helloWorldActionListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class BtnEventHandler implements EventHandler<ActionEvent> {
    /**
     * Invoked when a specific event of the type for which this handler is
     * registered happens.
     *
     * @param event the event which occurred
     */
    @Override
    public void handle(ActionEvent event) {
        System.out.println("Button pressed!");
    }
}
