package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RecipeIngredientActivityWidget extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int RECIPE_INGREDIENT_LOADER_ID = 3;

    //Recipes mRecipes;
    Context mContext;

    // Member variables for the adapter and RecyclerView
    private IngredientCursorAdapterWidget mAdapter;
    @BindView(R.id.rv_ingredients_widget)RecyclerView mRecyclerView;


    int recipeId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients_widget);

        mContext = this;

//        Intent intent = getIntent();
//        if(intent.hasExtra("RECIPE_ID")) {
//            recipeId = intent.getIntExtra("RECIPE_ID", 1);
//        }
         //We will get the preferred recipe_id from shared preferences now.
        SharedPreferences sharedPref = mContext.getSharedPreferences(getString(R.string.app_shared_pref_file), Context.MODE_PRIVATE) ;
        recipeId = sharedPref.getInt(getString(R.string.key_favorite_recipe_id), 1);

        // Bind the views
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new IngredientCursorAdapterWidget(this);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(RECIPE_INGREDIENT_LOADER_ID, null, RecipeIngredientActivityWidget.this);

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
