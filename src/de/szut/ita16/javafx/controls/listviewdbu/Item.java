package de.szut.ita16.javafx.controls.listviewdbu;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Item {
    public Item(String value) {
        this(null, value); // call AllArgsConstructor
    }

    private Integer id;
    private String value;

    public void setId(Integer id) {
        if(this.id == null) {
            this.id = id;
        } else {
            throw new IllegalStateException("ID Change not allowed.");
        }
    }
}
