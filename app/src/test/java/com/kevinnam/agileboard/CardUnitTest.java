package com.kevinnam.agileboard;

import com.kevinnam.agileboard.models.Card;
import com.kevinnam.agileboard.models.Column;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Card local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CardUnitTest {

    @Test
    public void card_CreateAndReadTest() throws Exception {
        String title = "cardTitle";
        String description = "cardDescription";
        int estimate = 21;

        Card card = new Card(title, description, estimate);
        assertEquals("Card Title", card.getTitle(), title);
        assertEquals("Card Description", card.getDescription(), description);
        assertEquals("Card Estimate", card.getEstimate(), estimate);
    }

    @Test
    public void card_ColumnStatusSetAndGetTest() throws Exception {
        Card card = new Card("title", "description", 21);

        // test if the initial column is "TO DO"
        assertEquals("Initial column", card.getColumnKey(), Column.KEY_TODO);

        String columnStatus = Column.KEY_DONE;
        card.setColumnKey(columnStatus);
        assertEquals("Initial column", card.getColumnKey(), columnStatus);
    }
}