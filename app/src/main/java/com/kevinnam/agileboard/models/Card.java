package com.kevinnam.agileboard.models;

import android.support.annotation.IntRange;

/**
 * Card model class
 * Created by kevin on 14/05/2017.
 */

public class Card {
    public final static int MIN_ESTIMATE_POINT = 1;
    public final static int MAX_ESTIMATE_POINT = 99;

    private String title;
    private String description;
    private int estimate;
    private String columnKey;

    public Card() {
        this.estimate = MIN_ESTIMATE_POINT;
    }

    public Card(String title, String description, @IntRange(from=MIN_ESTIMATE_POINT,to=MAX_ESTIMATE_POINT) int estimate) {
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
