package com.example.uadnd.cou8901.bakersresourceapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.uadnd.cou8901.bakersresourceapp.R;
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

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        String widgetText =  context.getString(R.string.appwidget_text);
        int recipeId ;
        //Get Favorite Recipe from Shared Preferences .
        // Maintained by App based on most recent visit to Step Activity
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_shared_pref_file), Context.MODE_PRIVATE) ;
        recipeId = Integer.parseInt(sharedPref.getString(context.getString(R.string.key_favorite_recipe_id), "1"));
        Timber.d("updateAppWidget:Favorite Recipe Id=>" + recipeId);


        // Create an Intent to launch IngredientActivity . this is not part of main application.
        Intent intent = new Intent(context, RecipeIngredientActivityWidget.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_app_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText + recipeId);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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
}

