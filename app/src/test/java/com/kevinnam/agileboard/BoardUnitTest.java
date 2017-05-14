package com.kevinnam.agileboard;

import com.kevinnam.agileboard.models.Board;
import com.kevinnam.agileboard.models.Column;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Board local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardUnitTest {

    @Test
    public void board_CreateTest() throws Exception {
        Board board = new Board();

        assertEquals(board.getColumnList().size(), 0);
    }

    @Test
    public void board_CreateWithColumnsTest() throws Exception {

        List<Column> columnList = new ArrayList<>();
        // create the columns
        columnList.add(new Column("To Do", Column.KEY_TODO, -1));
        columnList.add(new Column("Starting", Column.KEY_STARTING, 50));
        columnList.add(new Column("Code Review", Column.KEY_CODE_REVIEW, 100));
        columnList.add(new Column("Done", Column.KEY_DONE, -1));

        Board board = new Board(columnList);

        int expected = columnList.size();
        assertEquals(board.getColumnList().size(), expected);
    }

    @Test
    public void board_FindColumnByKeyTest() throws Exception {
        String columnKey = Column.KEY_CODE_REVIEW;
        Board board = setupBasicBoard();
        Column column = board.getColumnByKey(columnKey);
        assertEquals(column.getKey(), columnKey);
    }

    private Board setupBasicBoard() {
        List<Column> columnList = new ArrayList<>();
        // create the columns
        columnList.add(new Column("To Do", Column.KEY_TODO, -1));
        columnList.add(new Column("Starting", Column.KEY_STARTING, 50));
        columnList.add(new Column("Code Review", Column.KEY_CODE_REVIEW, 100));
        columnList.add(new Column("Done", Column.KEY_DONE, -1));
        // create a Board
        Board board = new Board(columnList);

        return board;
    }
}