package com.kevinnam.agileboard;

import com.kevinnam.agileboard.models.Column;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Column local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ColumnUnitTest {

    @Test
    public void column_CreateAndReadTest() throws Exception {
        String name = "columnName";
        String status = "columnStatus";
        int limitOfWIP = 99;

        Column column = new Column(name, status, limitOfWIP);
        assertEquals("Column name", column.getName(), name);
        assertEquals("Column Status", column.getKey(), status);
        assertEquals("Column LimitOfWIP", column.getLimitOfWIP(), limitOfWIP);
    }
}