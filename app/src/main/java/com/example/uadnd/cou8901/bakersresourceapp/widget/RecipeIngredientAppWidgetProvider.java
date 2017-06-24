package com.example.uadnd.cou8901.bakersresourceapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.uadnd.cou8901.bakersresourceapp.R;
import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;
import com.example.uadnd.cou8901.bakersresourceapp.ui.RecipeIngredientActivity;
import com.example.uadnd.cou8901.bakersresourceapp.ui.RecipeIngredientActivityWidget;
import com.example.uadnd.cou8901.bakersresourceapp.ui.RecipeMainActivity;
import com.example.uadnd.cou8901.bakersresourceapp.ui.RecipeMainActivityWidget;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Timber.d("RecipeIngredientAppWidgetProvider:updateAppWidget========================================================");
        //CharSequence recipeName = context.getString(R.string.appwidget_text);
        int recipeId ;
        //Get Favorite Recipe from Shared Preferences .
        // Maintained by App based on most recent visit to Step Activity
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_shared_pref_file), Context.MODE_PRIVATE) ;
        recipeId = Integer.parseInt(sharedPref.getString(context.getString(R.string.key_favorite_recipe_id), "1"));

        String recipeName =  context.getString(R.string.appwidget_text);
        String recipeIngredients =  getIngredients(context, recipeId) ; //context.getString(R.string.appwidget_text);

        recipeName = sharedPref.getString(context.getString(R.string.key_favorite_recipe_name), "Nutella Pie");
        Timber.d("updateAppWidget:Favorite Recipe Id=>" + recipeId);


        // Create an Intent to launch IngredientActivity . this is not part of main application.
        Intent intent = new Intent(context, RecipeIngredientActivityWidget.class);
        //
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_app_widget);
        //views.setTextViewText(R.id.appwidget_text, recipeName + recipeId);
        views.setTextViewText(R.id.appwidget_recipe, recipeName + " : Ingredients");
        views.setTextViewText(R.id.appwidget_ingredients, recipeIngredients);
        //views.setOnClickPendingIntent(R.id.appwidget_recipe, pendingIntent);
        //views.setOnClickPendingIntent(R.id.appwidget_ingredients, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            // There may be multiple widgets active, so update all of them
            Timber.d("RecipeIngredientAppWidgetProvider:onUpdate");
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//        int recipeId ;
//        String widgetText = "Hello, World";
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_shared_pref_file), Context.MODE_PRIVATE) ;
//        recipeId = Integer.parseInt(sharedPref.getString(context.getString(R.string.key_favorite_recipe_id), "1"));
//        widgetText = sharedPref.getString(context.getString(R.string.key_favorite_recipe_name), "Nutella Pie");
//        Timber.d("onReceive:Favorite Recipe Id=>" + recipeId );
//        Timber.d("onReceive:Favorite Recipe Name=>" + widgetText );
//
//
//    }

    private static String getIngredients(Context context, int recipeId){
        String ingredients = "Ingredient / Qty / Meas";
        String ingredient = null;
        String quantity = null;
        String measure = null;


        Cursor cursor = context.getContentResolver().query(BakersResourceContract.Ingredients.INGREDIENTS_URI,
                null,
                " recipe_id = " + recipeId + " ", //null,
                null,
                BakersResourceContract.Ingredients.COLUMN_INGREDIENT);  // order steps by step_id
        if(cursor != null && cursor.getCount() > 0) {

            int recipeIdIndex = cursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_RECIPE_ID);
            int ingredientIndex = cursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_INGREDIENT);
            int measureIndex = cursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_MEASURE);
            int quantityIndex = cursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_QUANTITY);
            cursor.moveToFirst();
            int i = 0;
            while(!cursor.isAfterLast()) {
                ingredient = cursor.getString(ingredientIndex);
                quantity = cursor.getString(quantityIndex);
                measure = cursor.getString(measureIndex);
                i++;


                ingredients = ingredients + "\n" + i + ". " + ingredient + " / " + quantity + " / " + measure;
                Timber.d(ingredient);
                cursor.moveToNext();

            }
            cursor.close();
        }
        return ingredients;



//
//        return "Bittersweet chocolate (60-70% cacao)\n" +
//                "unsalted butter\n" +
//                "granulated sugar\n" +
//                "light brown sugar\n" +
//                "large eggs\n" +
//                "vanilla extract\n" +
//                "all purpose flour\n" +
//                "cocoa powder\n" +
//                "salt\n" +
//                "semisweet chocolate chips";
    }
}

