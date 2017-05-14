package com.kevinnam.agileboard.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 14/05/2017.
 */

public class Iteration {
    Board parentBoard;
    List<Card> cardList;    // all cards list
    Map<String, List<Card>> mapColumnCardList;  // map for the card lists in the each column
    Card lastMovedCard;
    Column undoColumn;

    public Iteration(Board board) {
        parentBoard = board;
        List<Column> columnList = parentBoard.getColumnList();
        mapColumnCardList = new HashMap<>();
        for(Column column: columnList) {
            List<Card> cardList = new ArrayList<>();
            mapColumnCardList.put(column.getKey(), cardList);
        }
        cardList = new ArrayList<>();
    }

    /**
     * Add a card to the all list and initial column list(TO DO)
     * @param card to be put
     * @return true if succeeded to add
     */
    public boolean add(Card card) {
        List<Card> columnCardList = mapColumnCardList.get(card.getColumnKey());
        columnCardList.add(card);
        return cardList.add(card);
    }

    public Card getCard(int index) {
        return cardList.get(index);
    }

    /**
     * @return size of all cards list
     */
    public int size() {
        return cardList.size();
    }

    public int velocity() {
        List<Card> doneCardList = mapColumnCardList.get(Column.KEY_DONE);
        int velocity = 0;
        for(Card card: doneCardList) {
            velocity += card.getEstimate();
        }

        return velocity;
    }

    public void moveCard(Card card, Column toColumn) {
        moveCard(card, toColumn, true);
    }

    public void moveCard(Card destCard, Column toColumn, boolean keepUndo) {
        List<Card> fromColumnCardList = mapColumnCardList.get(destCard.getColumnKey());
        List<Card> toColumnCardList = mapColumnCardList.get(toColumn.getKey());

        int limit = toColumn.getLimitOfWIP();
        if(limit > 0) {
            int sumOfEstimate = 0;
            for (Card card : toColumnCardList) {
                sumOfEstimate += card.getEstimate();
            }

            if (sumOfEstimate + destCard.getEstimate() > limit) {
                throw new RuntimeException("Couldn't move this card to the column due to the WIP limit");
            }
        }

        if(fromColumnCardList.remove(destCard)) {

            if(toColumnCardList.add(destCard)) {
                if(keepUndo) {
                    lastMovedCard = destCard;
                    undoColumn =  parentBoard.getColumnByKey(destCard.getColumnKey());
                } else {
                    lastMovedCard = null;
                    undoColumn = null;
                }
                destCard.setColumnKey(toColumn.getKey());
            }
        } else {
            throw new RuntimeException("Couldn't find the card in the column");
        }
    }

    public boolean undoLastMove() {
        if(lastMovedCard != null && undoColumn != null) {
            moveCard(lastMovedCard, undoColumn, false);
            return true;
        }
        return false;
    }

    public int getColumnCardListSize(Column column) {
        List<Card> cardList = getCardListByColumn(column);
        if(cardList != null) {
            return cardList.size();
        }
        return 0;
    }

    public Card getCardInColumn(Column column, int index) {
        List<Card> cardList = getCardListByColumn(column);
        if(cardList != null && cardList.size() > index) {
            return cardList.get(index);
        }
        return  null;
    }

    public List<Card> getCardListByColumn(Column column) {
        return mapColumnCardList.get(column.getKey());
    }
}
