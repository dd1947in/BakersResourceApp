package com.example.uadnd.cou8901.bakersresourceapp.cp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dd2568 on 6/6/2017.
 */

public class BakersResourceSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bakersResource.db";
    public static final int VERSION = 1;

    public BakersResourceSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BakersResourceSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    public  BakersResourceSQLiteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        // Create favorite_movies table
//        final String FAVORITE_MOVIES_CREATE_TABLE = " CREATE TABLE "  + FavoriteMoviesContract.FavoriteMovies.TABLE_NAME + " (" +
//                FavoriteMoviesContract.FavoriteMovies._ID                + " INTEGER PRIMARY KEY, " +
//                FavoriteMoviesContract.FavoriteMovies.COLUMN_ID + " TEXT  UNIQUE  NOT NULL ); " ;
//
//        db.execSQL(FAVORITE_MOVIES_CREATE_TABLE);

        final String RECIPES_TABLE = " CREATE TABLE " + BakersResourceContract.Recipes.TABLE_NAME + " (" +
                BakersResourceContract.Recipes._ID + " INTEGER PRIMARY KEY, " +   // PK from base columns
                BakersResourceContract.Recipes.COLUMN_RECIPE_ID + " INTEGER NOT NULL UNIQUE , " +  // Unique Key
                BakersResourceContract.Recipes.COLUMN_NAME + " TEXT, " +
                BakersResourceContract.Recipes.COLUMN_SERVINGS + " TEXT, " +
                BakersResourceContract.Recipes.COLUMN_IMAGE  + " TEXT ); " ;

        final String STEPS_TABLE = " CREATE TABLE " + BakersResourceContract.Steps.TABLE_NAME + " (" +
                BakersResourceContract.Steps._ID + " INTEGER PRIMARY KEY, " + // PK from base columns
                BakersResourceContract.Steps.COLUMN_RECIPE_ID + " INTEGER  NOT NULL , " +
                BakersResourceContract.Steps.COLUMN_STEP_ID + " INTEGER  NOT NULL , " +
                BakersResourceContract.Steps.COLUMN_SHORT_DESCRIPTION + " TEXT, " +
                BakersResourceContract.Steps.COLUMN_DESCRIPTION + " TEXT, " +
                BakersResourceContract.Steps.COLUMN_VIDEO_URL + " TEXT, " +
                BakersResourceContract.Steps.COLUMN_THUMB_NAIL_URL  + " TEXT, " +
                BakersResourceContract.Steps.COLUMN_UKEY  + " TEXT  NOT NULL UNIQUE ); " ; // Unique Key Added after review 2

        final String INGREDIENTS_TABLE = " CREATE TABLE " + BakersResourceContract.Ingredients.TABLE_NAME + " (" +
                BakersResourceContract.Ingredients._ID + " INTEGER PRIMARY KEY, " + // PK from base columns
                BakersResourceContract.Ingredients.COLUMN_RECIPE_ID + " INTEGER, " +
                BakersResourceContract.Ingredients.COLUMN_QUANTITY + " FLOAT, " +
                BakersResourceContract.Ingredients.COLUMN_MEASURE + " TEXT, " +
                BakersResourceContract.Ingredients.COLUMN_INGREDIENT  + " TEXT  NOT NULL, " +
                BakersResourceContract.Steps.COLUMN_UKEY  + " TEXT  NOT NULL UNIQUE ); " ; // Unique Key Added after review 2

        db.execSQL(RECIPES_TABLE);
        db.execSQL(STEPS_TABLE);
        db.execSQL(INGREDIENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        final String FAVORITE_MOVIES_DROP_TABLE = "DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMovies.TABLE_NAME;
//        db.execSQL(FAVORITE_MOVIES_DROP_TABLE);
//        onCreate(db);
        final String RECIPES_DROP_TABLE = " DROP TABLE IF EXISTS " + BakersResourceContract.Recipes.TABLE_NAME;
        final String STEPS_DROP_TABLE = " DROP TABLE IF EXISTS " + BakersResourceContract.Steps.TABLE_NAME;
        final String INGREDIENTS_DROP_TABLE = " DROP TABLE IF EXISTS " + BakersResourceContract.Ingredients.TABLE_NAME;
        db.execSQL(STEPS_DROP_TABLE);
        db.execSQL(INGREDIENTS_DROP_TABLE);
        db.execSQL(RECIPES_DROP_TABLE);
        onCreate(db);
    }
    public void onRefreshRecipes(SQLiteDatabase db) {
        final String RECIPES_DROP_TABLE = " DROP TABLE IF EXISTS " + BakersResourceContract.Recipes.TABLE_NAME;
        final String STEPS_DROP_TABLE = " DROP TABLE IF EXISTS " + BakersResourceContract.Steps.TABLE_NAME;
        final String INGREDIENTS_DROP_TABLE = " DROP TABLE IF EXISTS " + BakersResourceContract.Ingredients.TABLE_NAME;
        db.execSQL(STEPS_DROP_TABLE);
        db.execSQL(INGREDIENTS_DROP_TABLE);
        db.execSQL(RECIPES_DROP_TABLE);
        onCreate(db);
    }
}
