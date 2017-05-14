package com.kevinnam.agileboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * NewCardActivity is the Activity which a user input a new card information.
 * Created by kevin on 14/05/2017.
 */

public class NewCardActivity extends AppCompatActivity {
    public final static String EXTRA_NAME_CARD_TITLE = "card_title";
    public final static String EXTRA_NAME_CARD_DESCRIPTION = "card_description";
    public final static String EXTRA_NAME_CARD_ESTIMATE = "card_estimate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_card) {
            saveCard();
            return true;
        } else if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCard() {
        EditText etTitle = (EditText)findViewById(R.id.etTitle);
        EditText etDescription = (EditText)findViewById(R.id.etDescription);
        EditText etEstimate = (EditText)findViewById(R.id.etEstimate);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_NAME_CARD_TITLE, etTitle.getText().toString().trim());
        resultIntent.putExtra(EXTRA_NAME_CARD_DESCRIPTION, etDescription.getText().toString().trim());
        try {
            int estimate =  Integer.parseInt(etEstimate.getText().toString());
            resultIntent.putExtra(EXTRA_NAME_CARD_ESTIMATE, estimate);
        } catch(NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
