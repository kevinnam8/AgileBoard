package com.kevinnam.agileboard.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Board model class
 * Created by kevin on 14/05/2017.
 */

public class Board {

    private List<Column> columnList;
    private List<Iteration> iterationList;

    public Board() {
        columnList = new ArrayList<>();
        iterationList = new ArrayList<>();
        iterationList.add(new Iteration(this));
    }

    public Board(List<Column> columns) {
        columnList = columns;
        iterationList = new ArrayList<>();
        iterationList.add(new Iteration(this));
    }

    public void addColumn(Column column) {
        columnList.add(column);
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public Iteration getIteration() {
        return getIteration(0);
    }

    public Iteration getIteration(int index) {
        if(iterationList != null && iterationList.size() > index) {
            return iterationList.get(index);
        }
        return null;
    }

    public Column getColumnByKey(String key) {
        for(Column column: columnList) {
            if(column.getKey().equals(key)) {
                return column;
            }
        }
        return null;
    }
}
