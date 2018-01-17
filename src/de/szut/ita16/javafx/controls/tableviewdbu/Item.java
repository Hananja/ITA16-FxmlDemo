package de.szut.ita16.javafx.controls.tableviewdbu;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Item {
    public Item(String value) {
        this(null, value, 0); // call AllArgsConstructor
    }
    public Item(String value, int count) {
        this(null, value, count); // call AllArgsConstructor
    }

    private Integer id;
    private String value;
    private int count;

    public void setId(Integer id) {
        if(this.id == null) {
            this.id = id;
        } else {
            throw new IllegalStateException("ID Change not allowed.");
        }
    }
}
