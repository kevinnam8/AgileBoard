package com.kevinnam.agileboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.kevinnam.agileboard.models.Board;
import com.kevinnam.agileboard.models.Card;
import com.kevinnam.agileboard.models.Column;
import com.kevinnam.agileboard.models.Iteration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_NEW_CARD = 1001;

    private Board mAgileBoard;
    private Iteration mCurrentIteration;
    private RecyclerView mRvIteration;
    private IterationRecyclerAdapter mIterationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to the New card activity.
                Intent intent = new Intent(getBaseContext(), NewCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_CARD);
            }
        });

        setupAgileBoard();
        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_undo) {
            if(mCurrentIteration.undoLastMove()) {
                updateVelocity();
                mIterationAdapter.notifyDataSetChanged();
            }
            return true;
        } else if(id == R.id.action_choose_column) {
            showChooseColumnDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW_CARD) {
            if(resultCode == RESULT_OK) {
                String title = data.getStringExtra(NewCardActivity.EXTRA_NAME_CARD_TITLE);
                String description = data.getStringExtra(NewCardActivity.EXTRA_NAME_CARD_DESCRIPTION);
                int estimate = data.getIntExtra(NewCardActivity.EXTRA_NAME_CARD_ESTIMATE, Card.MIN_ESTIMATE_POINT);
                Card card = new Card(title, description, estimate);

                mCurrentIteration.add(card);
                mIterationAdapter.notifyDataSetChanged();
                updateVelocity();

            }
        }
    }

    /**
     * setup the agile board and columns
     */
    void setupAgileBoard() {

        List<Column> columnList = new ArrayList<>();
        // create the starting column
        columnList.add(new Column("To Do", Column.KEY_TODO, -1));
        columnList.add(new Column("Starting", Column.KEY_STARTING, 100));
        columnList.add(new Column("Code Review", Column.KEY_CODE_REVIEW, 100));
        columnList.add(new Column("Done", Column.KEY_DONE, -1));
        // create a Board
        mAgileBoard = new Board(columnList);
        mCurrentIteration = mAgileBoard.getIteration();
    }

    /**
     * setup the iteration's recycler view and adapter
     */
    void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvIteration = (RecyclerView)findViewById(R.id.rvIteration);
        mRvIteration.setLayoutManager(layoutManager);
        mIterationAdapter = new IterationRecyclerAdapter(getBaseContext(), mAgileBoard);
        mRvIteration.setAdapter(mIterationAdapter);

        mIterationAdapter.setOnItemClickListener(new IterationRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, Card card) {
                showMoveCardDialog(card);
            }
        });
    }

    /**
     * Display the current velocity on the action bar
     */
    void updateVelocity() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int velocity = mCurrentIteration.velocity();
            actionBar.setTitle(getString(R.string.format_velocity, velocity));
        }
    }

    void showMoveCardDialog(final Card card) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(R.string.move_card);

        int checkedIndex = 0;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        final List<Column> columnList = mAgileBoard.getColumnList();
        for(int i=0; i < columnList.size(); i++) {
            Column column = columnList.get(i);
            if(card.getColumnKey().equals(column.getKey())) {
                checkedIndex = i;
            }
            arrayAdapter.add(column.getName());
        }

        builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setSingleChoiceItems(arrayAdapter, checkedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                Column toColumn = columnList.get(which);
                try {
                    mCurrentIteration.moveCard(card, toColumn);
                    mIterationAdapter.notifyDataSetChanged();
                    updateVelocity();
                } catch (RuntimeException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    void showChooseColumnDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(R.string.choose_column);

        int checkedIndex = 0;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        final List<Column> columnList = mAgileBoard.getColumnList();
        arrayAdapter.add("All");
        for(int i=0; i < columnList.size(); i++) {
            Column column = columnList.get(i);
            arrayAdapter.add(column.getName());
        }

        builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setSingleChoiceItems(arrayAdapter, checkedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    // all
                    mIterationAdapter.setFilterColumn(null);
                } else {
                    Column column = columnList.get(which-1);
                    mIterationAdapter.setFilterColumn(column);
                }
                mIterationAdapter.notifyDataSetChanged();
                updateVelocity();
                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

}
