package com.kevinnam.agileboard.models;

/**
 * Card model class
 * Created by kevin on 14/05/2017.
 */

public class Card {
    private String title;
    private String description;
    private int estimate;
    private String columnKey;

    public Card() {
        this.estimate = 1;
    }

    public Card(String title, String description, int estimate) {
        this.title = title;
        this.description = description;
        this.estimate = estimate;
        this.columnKey = Column.KEY_TODO;     //default column
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

}
