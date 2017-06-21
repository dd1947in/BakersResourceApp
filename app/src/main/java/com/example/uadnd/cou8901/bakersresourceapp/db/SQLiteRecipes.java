package com.example.uadnd.cou8901.bakersresourceapp.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;

import java.util.ArrayList;

import timber.log.Timber;

import static java.security.AccessController.getContext;

/**
 * Created by dd2568 on 6/10/2017.
 */

public  class SQLiteRecipes {
    public static void persistIngredient(Context context, Ingredient ingredient) {
        //Timber.d("persistIngredient");
        Uri uri;
        ContentValues contentValues = new ContentValues();

        contentValues.put(BakersResourceContract.Ingredients.COLUMN_RECIPE_ID, ingredient.getRecipe_id());
        contentValues.put(BakersResourceContract.Ingredients.COLUMN_QUANTITY, ingredient.getQuantity());
        contentValues.put(BakersResourceContract.Ingredients.COLUMN_MEASURE, ingredient.getMeasure());
        contentValues.put(BakersResourceContract.Ingredients.COLUMN_INGREDIENT, ingredient.getIngredient());

        ContentResolver contentResolver = context.getContentResolver();

        uri = contentResolver.insert(BakersResourceContract.Ingredients.INGREDIENTS_URI, contentValues);

    }

    public static void persistStep(Context context, Step step) {
        //Timber.d("persistStep");
        Uri uri;
        ContentValues contentValues = new ContentValues();

        contentValues.put(BakersResourceContract.Steps.COLUMN_RECIPE_ID, step.getRecipe_id());
        contentValues.put(BakersResourceContract.Steps.COLUMN_STEP_ID, step.getStep_id());
        contentValues.put(BakersResourceContract.Steps.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
        contentValues.put(BakersResourceContract.Steps.COLUMN_DESCRIPTION, step.getDescription());
        contentValues.put(BakersResourceContract.Steps.COLUMN_VIDEO_URL, step.getVideoURL());
        contentValues.put(BakersResourceContract.Steps.COLUMN_THUMB_NAIL_URL, step.getThumbnailURL());

        ContentResolver contentResolver = context.getContentResolver();

        uri = contentResolver.insert(BakersResourceContract.Steps.STEPS_URI, contentValues);

    }

    public static void persistRecipe(Context context, Recipe recipe) {
        //Timber.d("persistRecipe");
        Uri uri;
        ContentValues contentValues = new ContentValues();

        contentValues.put(BakersResourceContract.Recipes.COLUMN_RECIPE_ID, recipe.getRecipe_id());
        contentValues.put(BakersResourceContract.Recipes.COLUMN_NAME, recipe.getName());
        contentValues.put(BakersResourceContract.Recipes.COLUMN_SERVINGS, recipe.getServings());
        contentValues.put(BakersResourceContract.Recipes.COLUMN_IMAGE, recipe.getImage());

        ContentResolver contentResolver = context.getContentResolver();

        uri = contentResolver.insert(BakersResourceContract.Recipes.RECIPES_URI, contentValues);



    }

    public static void persistRecipes(Context context, Recipes recipes) {
        //Timber.d("persistRecipes");
        int recipe_count = recipes.recipes.size();
        int recipe_id ;
        for(int i = 0; i < recipe_count; i++) {
            Recipe recipe = recipes.recipes.get(i);
            recipe_id = recipe.getId();
            recipe.setRecipe_id(recipe_id);
            ArrayList<Step> steps = recipe.getSteps();
            ArrayList<Ingredient> ingredients = recipe.getIngredients();
            int step_count = steps.size();
            persistRecipe(context, recipe);
            for(int j = 0; j < step_count; j++) {
                Step step = steps.get(j);
                int step_id = step.getId();
                step.setRecipe_id(recipe_id);
                step.setStep_id(step_id);
                persistStep(context, step);
            }
            int ingr_count = ingredients.size();
            for(int j = 0; j < ingr_count; j++) {
                Ingredient ingredient = ingredients.get(j);
                ingredient.setRecipe_id(recipe_id);
                persistIngredient(context, ingredient);
            }
        }

    }

}
