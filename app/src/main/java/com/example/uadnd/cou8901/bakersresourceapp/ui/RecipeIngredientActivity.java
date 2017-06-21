package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.uadnd.cou8901.bakersresourceapp.R;
import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeIngredientActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int RECIPE_STEP_LOADER_ID = 1;

    //Recipes mRecipes;
    Context mContext;

    // Member variables for the adapter and RecyclerView
    private IngredientCursorAdapter mAdapter;
    @BindView(R.id.rv_ingredients)RecyclerView mRecyclerView;
    //@BindView(R.id.tv_switch_to_steps) TextView mStepsView;

    int recipeId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredient);

        mContext = this;
        Intent intent = getIntent();
        if(intent.hasExtra("RECIPE_ID")) {
            recipeId = intent.getIntExtra("RECIPE_ID", 1);
        }
        // Bind the views
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new IngredientCursorAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
/**********************************************************
 * Design changed but keeping the code in comment
        mStepsView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Timber.d("onClick");
                //Toast.makeText(mContext, "On Click Listener:" + recipeId, Toast.LENGTH_LONG).show();
                Intent recipeStepIntent = new Intent(mContext, RecipeStepActivity.class );
                recipeStepIntent.putExtra("RECIPE_ID", recipeId);
                //Bundle bundle = new Bundle();
                //bundle.putString("RECIPE_ID", String.valueOf(recipeId) );
                startActivity(recipeStepIntent);

            }
        });
***************************************************/

        getSupportLoaderManager().initLoader(RECIPE_STEP_LOADER_ID, null, RecipeIngredientActivity.this);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int scrollX = mRecyclerView.getScrollX();
        int scrollY = mRecyclerView.getScrollY();
        outState.putInt(getString(R.string.SCROLLX), scrollX);
        outState.putInt(getString(R.string.SCROLLY), scrollY);
        //Timber.d("onSaveInstanceState : " + scrollX + ":" + scrollY );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int scrollX = savedInstanceState.getInt(getString(R.string.SCROLLX));
        int scrollY = savedInstanceState.getInt(getString(R.string.SCROLLY));
        //Timber.d("onRestoreInstanceState : " + scrollX + ":" + scrollY );
        mRecyclerView.scrollTo(scrollX, scrollY);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mStepData = null;

            @Override
            protected void onStartLoading() {
                Timber.d("onStartLoading");
                if(mStepData != null) {
                    deliverResult(mStepData);
                } else {
                    //Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                Timber.d("loadInBackground");

                try {
                    return getContentResolver().query(BakersResourceContract.Ingredients.INGREDIENTS_URI,
                            null,
                            " recipe_id = " + recipeId + " ", //null,
                            null,
                            BakersResourceContract.Ingredients.COLUMN_INGREDIENT);  // order steps by step_id


                }catch (Exception e) {
                    Timber.d(e);
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                Timber.d("deliverResult=" + data.getCount());
                mStepData = data;
                super.deliverResult(data);

                // Test Code  just loop through curson and print result
                int recipeIdIndex = data.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_RECIPE_ID);
                int ingredientIndex = data.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_INGREDIENT);
                int measureIndex = data.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_MEASURE);
                int quantityIndex = data.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_QUANTITY);
                data.moveToFirst();

//                while(!data.isAfterLast()) {
//                    Timber.d(String.valueOf(data.getInt(recipeIdIndex)) + ":" + data.getString(ingredientIndex) + ":" +
//                            data.getString(measureIndex) + ":" + data.getString(quantityIndex));
//                    data.moveToNext();
//                }
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Timber.d("onLoadFinished");
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.d("onLoaderReset");
    }
}
