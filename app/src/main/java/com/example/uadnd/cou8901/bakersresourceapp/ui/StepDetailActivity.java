package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.uadnd.cou8901.bakersresourceapp.R;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle(R.string.title_step_detail);
            //actionBar.show();
            //Toast.makeText(this, getString(R.string.title_step_detail), Toast.LENGTH_LONG).show();
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
//            arguments.putString(StepDetailFragment.ARG_ITEM_ID,
//                    getIntent().getStringExtra(StepDetailFragment.ARG_ITEM_ID));
            arguments.putString(StepDetailFragment.STEP_ID,
                    getIntent().getStringExtra(StepDetailFragment.STEP_ID));
            //Pack all attributes into arguments here use them in Step Detail Fragment
            arguments.putString(StepDetailFragment.STEP_ID,
                    getIntent().getStringExtra(StepDetailFragment.STEP_ID));
            arguments.putString(StepDetailFragment.STEP_SHORT_DESCRIPTION,
                    getIntent().getStringExtra(StepDetailFragment.STEP_SHORT_DESCRIPTION));
            arguments.putString(StepDetailFragment.STEP_DESCRIPTION,
                    getIntent().getStringExtra(StepDetailFragment.STEP_DESCRIPTION));
            arguments.putString(StepDetailFragment.STEP_VEDEO_URL,
                    getIntent().getStringExtra(StepDetailFragment.STEP_VEDEO_URL));
            arguments.putString(StepDetailFragment.STEP_THUMB_NAIL_URL,
                    getIntent().getStringExtra(StepDetailFragment.STEP_THUMB_NAIL_URL));


            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, StepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
