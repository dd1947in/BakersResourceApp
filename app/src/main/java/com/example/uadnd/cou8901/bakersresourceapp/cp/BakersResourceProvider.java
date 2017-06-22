package com.example.uadnd.cou8901.bakersresourceapp.cp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import timber.log.Timber;

/**
 * Created by dd2568 on 6/6/2017.
 */

public final class BakersResourceProvider  extends ContentProvider {
    //public static final String AUTHORITY = "com.example.uadnd.cou8901.cp.provider";


    //All ID refer to _ID / PK field
    public static final int RECIPES = 100 ;   //select * from recipes
    public static final int RECIPES_WITH_ID = 101 ; // _ID


    public static final int STEPS = 200; //select * from steps
    public static final int STEPS_WITH_ID = 201;  //_ID

    //public static final int RECIPES_STEPS_WITH_ID = 203;  //Recipe_Id, id(step id) will be in URL

    public static final int INGREDIENTS = 300; //select * from ingredients
    public static final int INGREDIENTS_WITH_ID = 301; //_ID


    //public static final int RECIPES_INGREDIENTS_WITH_INGREDIENT = 303;  //Recipe_Id will be in URL

    //The following will be used for querying individual recipe details
    public static final int RECIPES_RECIPES = 102;  //Recipe_Id will be in URL
    public static final int RECIPES_STEPS = 202;  //Recipe_Id will be in URL
    public static final int RECIPES_INGREDIENTS = 302;  //Recipe_Id will be in URL


    private static final UriMatcher sUrimatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        // Init matcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //All Recipes
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_RECIPES_WITH_ID, RECIPES_WITH_ID); // _ID


        //All Steps
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_STEPS, STEPS);
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_STEPS_WITH_ID, STEPS_WITH_ID); // _ID

        //All Ingredients
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_INGREDIENTS_WITH_ID, INGREDIENTS_WITH_ID); // _ID


        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_RECIPES_RECIPES, RECIPES_RECIPES);  // "recipes_recipes/#"; // Recipe for a given recipe_id
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_RECIPES_STEPS, RECIPES_STEPS);  // "recipes_steps/#"; // Steps for a given recipe_id
        uriMatcher.addURI(BakersResourceContract.AUTHORITY, BakersResourceContract.PATH_RECIPES_INGREDIENTS, RECIPES_INGREDIENTS); // "recipes_ingredients/#"; // Ingredients for a given recipe_id



        return uriMatcher;
    }

    // Member variable for a TaskDbHelper that's initialized in the onCreate() method
    private BakersResourceSQLiteOpenHelper bakersResourceSQLiteOpenHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        bakersResourceSQLiteOpenHelper = new BakersResourceSQLiteOpenHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = bakersResourceSQLiteOpenHelper.getReadableDatabase();

        int match = sUrimatcher.match(uri);
        Cursor retCursor = null;

        switch (match) {
            case RECIPES :  // All Recipes
                retCursor =  db.query(BakersResourceContract.Recipes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case RECIPES_RECIPES:  // A Recipe for a given recipe_id
                retCursor =  db.query(BakersResourceContract.Recipes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case STEPS :
            case RECIPES_STEPS : // All Steps for a given recipe_id

                retCursor =  db.query(BakersResourceContract.Steps.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENTS :
            case RECIPES_INGREDIENTS : // All Ingredients for a given recipe_id
                retCursor =  db.query(BakersResourceContract.Ingredients.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;


        }

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = bakersResourceSQLiteOpenHelper.getWritableDatabase();

        int switch_match = sUrimatcher.match(uri);
        long id;
        Uri returnUri = null;
        switch (switch_match) {
            case RECIPES :
            case RECIPES_WITH_ID :
                //insert into recipes
                 //id = db.insert(BakersResourceContract.Recipes.TABLE_NAME, null, values);
                id = db.insertWithOnConflict(BakersResourceContract.Recipes.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                //id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(BakersResourceContract.Recipes.RECIPES_URI, id);
                }
                break;

            case STEPS :
            case STEPS_WITH_ID :
                //insert into steps
                 //id = db.insert(BakersResourceContract.Steps.TABLE_NAME, null, values);
                id = db.insertWithOnConflict(BakersResourceContract.Steps.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(BakersResourceContract.Steps.STEPS_URI, id);
                }

                break;

            case INGREDIENTS :
            case INGREDIENTS_WITH_ID :
                //insert into ingredients
                 //id = db.insert(BakersResourceContract.Ingredients.TABLE_NAME, null, values);
                id = db.insertWithOnConflict(BakersResourceContract.Ingredients.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(BakersResourceContract.Ingredients.INGREDIENTS_URI, id);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);

        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = bakersResourceSQLiteOpenHelper.getWritableDatabase();

        int switch_match = sUrimatcher.match(uri);
        int recordsDeleted = 0;
        String id = uri.getPathSegments().get(1);
        //could be : _id, recipe_id

        switch (switch_match) {

            case RECIPES_WITH_ID :
                //delete from recipes
                recordsDeleted = db.delete(BakersResourceContract.Recipes.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case STEPS_WITH_ID :
                //delete from steps
                recordsDeleted = db.delete(BakersResourceContract.Steps.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case INGREDIENTS_WITH_ID :
                //delete from ingredients
                recordsDeleted = db.delete(BakersResourceContract.Ingredients.TABLE_NAME, "_id=?", new String[]{id});
                break;

            case RECIPES_RECIPES :
                //Delete Ingredients, Steps, Recipe for a given recipe_id
                recordsDeleted = db.delete(BakersResourceContract.Steps.TABLE_NAME, "recipe_id=?", new String[]{id});
                recordsDeleted = db.delete(BakersResourceContract.Ingredients.TABLE_NAME, "recipe_id=?", new String[]{id});
                recordsDeleted = db.delete(BakersResourceContract.Recipes.TABLE_NAME, "recipe_id=?", new String[]{id});

            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);

        }
        return recordsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
