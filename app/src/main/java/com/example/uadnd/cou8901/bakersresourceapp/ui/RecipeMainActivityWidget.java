package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.uadnd.cou8901.bakersresourceapp.R;
import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;
import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceSQLiteOpenHelper;
import com.example.uadnd.cou8901.bakersresourceapp.db.Recipes;
import com.example.uadnd.cou8901.bakersresourceapp.db.SQLiteRecipes;
import com.example.uadnd.cou8901.bakersresourceapp.gson.GsonParser;
import com.example.uadnd.cou8901.bakersresourceapp.net.BakerRecipes;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeMainActivityWidget extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final String DATABASE_NAME = "bakersResource.db";
    private static final int RECIPE_LOADER_ID = 0;

    Context mContext;

    // Member variables for the adapter and RecyclerView
    private RecipeCursorAdapterWidget mAdapter;
    @BindView(R.id.rv_recipes_widget)RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up Timber
        Timber.plant(new Timber.DebugTree());

        super.onCreate(savedInstanceState);
        Timber.d("Hello, World");
        setContentView(R.layout.activity_recipe_main_widget);
        Timber.d("Hello, World");

        mContext = this;
        // Bind the views
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeCursorAdapterWidget(this);
        mRecyclerView.setAdapter(mAdapter);

        //loadRecipeDataInSQLite();

        // Moved to async background thread
        getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, RecipeMainActivityWidget.this);
    }
//    public void loadRecipeDataInSQLite() {
//
//        new BakerRecipeGetTask().execute();    //Async Task to get recipes and load them into SQLite
//
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mRecipeData = null;

            @Override
            protected void onStartLoading() {
                Timber.d("onStartLoading");
                if(mRecipeData != null) {
                    deliverResult(mRecipeData);
                } else {
                    //Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                Timber.d("loadInBackground");

                try {
                    return getContentResolver().query(BakersResourceContract.Recipes.RECIPES_URI,
                            null,
                            null,
                            null,
                            BakersResourceContract.Recipes.COLUMN_RECIPE_ID);


                }catch (Exception e) {
                    Timber.d(e);
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                Timber.d("deliverResult=" + data.getCount());
                mRecipeData = data;
                super.deliverResult(data);

                // Test Code  just loop through curson and print result
                int recipeIdIndex = data.getColumnIndex(BakersResourceContract.Recipes.COLUMN_RECIPE_ID);
                int nameIndex = data.getColumnIndex(BakersResourceContract.Recipes.COLUMN_NAME);
                int servingsIndex = data.getColumnIndex(BakersResourceContract.Recipes.COLUMN_SERVINGS);
                int imageIndex = data.getColumnIndex(BakersResourceContract.Recipes.COLUMN_IMAGE);
                data.moveToFirst();
//                while(!data.isAfterLast()) {
//                    Timber.d(String.valueOf(data.getInt(recipeIdIndex)) + ":" + data.getString(nameIndex) + ":" +
//                            data.getString(servingsIndex) + ":" + data.getString(imageIndex));
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
//    public class BakerRecipeGetTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            return BakerRecipes.GET(getString(R.string.baking_recipe_url));
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if(s.equals("")) {
//                return;
//            } else {
//                Recipes recipes = GsonParser.parse(s);
//                mContext.deleteDatabase(DATABASE_NAME);  // Delete the DB for a fresh load
//                SQLiteRecipes.persistRecipes(mContext, recipes);  // Create the new DB and load recipes
//                getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, RecipeMainActivityWidget.this); // Moved from onCreate()
//            }
//        }
//    }

}
