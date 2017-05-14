package com.kevinnam.agileboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kevinnam.agileboard.models.Board;
import com.kevinnam.agileboard.models.Card;
import com.kevinnam.agileboard.models.Column;
import com.kevinnam.agileboard.models.Iteration;

/**
 * IterationRecyclerAdapter is the Adapter for the card iteration recycler view
 * Created by kevin on 14/05/2017.
 */

public class IterationRecyclerAdapter extends RecyclerView.Adapter<IterationRecyclerAdapter.ViewHolder> {
    LayoutInflater mLayoutInflater;
    Board mBoard;
    Iteration mIteration;
    OnItemClickListener mClickListener;
    Column mFilterColumn;

    public IterationRecyclerAdapter(Context context, Board board) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBoard = board;
        mIteration = board.getIteration();
        mFilterColumn = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Card card;
        if(mFilterColumn == null) {
            card = mIteration.getCard(position);
        } else {
            card = mIteration.getCardInColumn(mFilterColumn, position);
        }

        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null) {
                    mClickListener.onClick(view, holder.getAdapterPosition(), card);
                }
            }
        });
        Column column = mBoard.getColumnByKey(card.getColumnKey());
        String columnName;
        if(column != null) {
            columnName = column.getName();
        } else {
            columnName = "undefined";
        }
        holder.bindView(card, columnName);
    }

    @Override
    public int getItemCount() {
        if(mFilterColumn == null) {
            return mIteration.size();
        } else {
            return mIteration.getColumnCardListSize(mFilterColumn);
        }
    }

    public void setFilterColumn(Column column) {
        if(mFilterColumn != column) {
            mFilterColumn = column;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvEstimate;
        TextView tvColumnName;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
            tvEstimate = (TextView)itemView.findViewById(R.id.tvEstimate);
            tvColumnName = (TextView)itemView.findViewById(R.id.tvColumn);
        }

        public void bindView(Card card, String columnName) {
            tvTitle.setText(card.getTitle());
            tvDescription.setText(card.getDescription());
            tvEstimate.setText(String.valueOf(card.getEstimate()));
            tvColumnName.setText(columnName);
        }
    }

    public interface OnItemClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v, int position, Card card);
    }
}
