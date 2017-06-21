package com.example.uadnd.cou8901.bakersresourceapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by dd2568 on 6/17/2017.
 */

public class RecipeIngredientAppWidgetService  extends IntentService {

    public static final String ACTION_GET_RECIPES = "ACTION_GET_RECIPES";


    public RecipeIngredientAppWidgetService() {
        super("RecipeIngredientAppWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
