package com.kevinnam.agileboard;

import com.kevinnam.agileboard.models.Board;
import com.kevinnam.agileboard.models.Card;
import com.kevinnam.agileboard.models.Column;
import com.kevinnam.agileboard.models.Iteration;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Iteration local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class IterationUnitTest {

    Board mBoard;
    Iteration mIteration;

    @Before
    public void setupBoardAndIteration() {
        List<Column> columnList = new ArrayList<>();
        // create the columns
        columnList.add(new Column("To Do", Column.KEY_TODO, -1));
        columnList.add(new Column("Starting", Column.KEY_STARTING, 50));
        columnList.add(new Column("Code Review", Column.KEY_CODE_REVIEW, 100));
        columnList.add(new Column("Done", Column.KEY_DONE, -1));
        // create a Board
        mBoard = new Board(columnList);
        mIteration = mBoard.getIteration();
    }

    @Test
    public void iteration_AddCardTest() throws Exception {
        Card card1 = new Card("CardTitle1", "description1", 21);
        Card card2 = new Card("CardTitle2", "description2", 30);
        mIteration.add(card1);
        mIteration.add(card2);

        // test the size ot card list
        int expected_size = 2;
        assertEquals(mIteration.size(), expected_size);

        Card retCard = mIteration.getCard(0);
        assertEquals(retCard.getTitle(), card1.getTitle());
        retCard = mIteration.getCard(1);
        assertEquals(retCard.getTitle(), card2.getTitle());
    }

    @Test
    public void iteration_MoveCardTest() throws Exception {
        Card card1 = new Card("CardTitle1", "description1", 21);
        Card card2 = new Card("CardTitle2", "description2", 34);
        mIteration.add(card1);
        mIteration.add(card2);
        mIteration.moveCard(card1, mBoard.getColumnByKey(Column.KEY_CODE_REVIEW));
        mIteration.moveCard(card2, mBoard.getColumnByKey(Column.KEY_CODE_REVIEW));

        int expected_size = 0;
        assertEquals(mIteration.getColumnCardListSize(mBoard.getColumnByKey(Column.KEY_TODO)),
                expected_size);

        expected_size = 2;
        assertEquals(mIteration.getColumnCardListSize(mBoard.getColumnByKey(Column.KEY_CODE_REVIEW)),
                expected_size);

        Card retCard = mIteration.getCardInColumn(mBoard.getColumnByKey(Column.KEY_CODE_REVIEW), 0);
        assertEquals(retCard.getTitle(), card1.getTitle());
        retCard = mIteration.getCardInColumn(mBoard.getColumnByKey(Column.KEY_CODE_REVIEW), 1);
        assertEquals(retCard.getTitle(), card2.getTitle());
    }

    /**
     * Req: You can calculate the velocity of a given iteration. This is defined as the sum of the points of all cards that are in the done column for an iteration.
     */
    @Test
    public void iteration_VelocityTest() throws Exception {
        int estimate1 = 21;
        int estimate2 = 34;
        Card card1 = new Card("CardTitle1", "description1", estimate1);
        Card card2 = new Card("CardTitle2", "description2", estimate2);
        mIteration.add(card1);
        mIteration.add(card2);

        // move card1 to DONE and test
        mIteration.moveCard(card1, mBoard.getColumnByKey(Column.KEY_DONE));
        assertEquals(mIteration.velocity(), estimate1);

        // move card2 to DONE and test
        mIteration.moveCard(card2, mBoard.getColumnByKey(Column.KEY_DONE));
        assertEquals(mIteration.velocity(), estimate1 + estimate2);
    }

    /**
     * Req: You can undo your last card column transition. So if you moved it to the done column, you can undo that move by calling a method
     */
    @Test
    public void iteration_UndoLastMoveTest() throws Exception {
        Card card1 = new Card("CardTitle1", "description1", 5);
        Card card2 = new Card("CardTitle2", "description2", 13);
        mIteration.add(card1);
        mIteration.add(card2);

        // move card1 to starting
        mIteration.moveCard(card1, mBoard.getColumnByKey(Column.KEY_STARTING));

        // move card2 to STARTING then CODE_REVIEW
        mIteration.moveCard(card2, mBoard.getColumnByKey(Column.KEY_STARTING));
        mIteration.moveCard(card2, mBoard.getColumnByKey(Column.KEY_CODE_REVIEW));

        // UNDO
        mIteration.undoLastMove();
        assertEquals(card2.getColumnKey(), Column.KEY_STARTING);
        assertEquals(card1.getColumnKey(), Column.KEY_STARTING);
    }

    /**
     * Req: You can enforce a work in progress limit (expressed in points) for a column.
     * If you try and add a card to a column that goes above the WIP limit, an exception should be thrown
     */
    @Test(expected= RuntimeException.class)
    public void iteration_PointsIsAboveLimitOfWIP() {
        Iteration iteration = mBoard.getIteration();

        Card card1 = new Card("CardTitle1", "description1", 21);
        iteration.add(card1);
        iteration.moveCard(card1, mBoard.getColumnByKey(Column.KEY_STARTING));

        Card card2 = new Card("CardTitle2", "description2", 34);
        iteration.add(card2);
        iteration.moveCard(card2, mBoard.getColumnByKey(Column.KEY_STARTING));
    }
}