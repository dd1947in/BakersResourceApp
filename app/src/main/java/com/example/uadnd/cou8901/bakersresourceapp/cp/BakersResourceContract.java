package com.example.uadnd.cou8901.bakersresourceapp.cp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dd2568 on 6/6/2017.
 */

public class BakersResourceContract {
    /**
     * Add content provider contract constants
     */
    //Authority
    public static final String AUTHORITY = "com.example.uadnd.cou8901.bakersresourceapp";

    //Base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //paths for recipe, steps, ingredients
    public static final String PATH_RECIPES = "recipes" ;
    public static final String PATH_RECIPES_WITH_ID = "recipes/#" ; // with pk

    //The following are used for query of one recipe, steps, ingredients based on recipe_id field
    public static final String PATH_RECIPES_RECIPES = "recipes_recipes/#"; // Recipe for a given recipe_id
    public static final String PATH_RECIPES_STEPS = "recipes_steps/#"; // Steps for a given recipe_id
    public static final String PATH_RECIPES_INGREDIENTS = "recipes_ingredients/#"; // Ingredients for a given recipe_id


    //Unused Paths but place holders for now
    public static final String PATH_STEPS = "steps" ;  // select * from steps
    public static final String PATH_STEPS_WITH_ID = "steps/#" ;  // with pk
    public static final String PATH_INGREDIENTS = "ingredients" ; // select * from ingredients
    public static final String PATH_INGREDIENTS_WITH_ID = "ingredients/#" ;


    /* Recipes table */
    public static final class Recipes implements BaseColumns {
        //Recipes URI
        public static final Uri RECIPES_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        //Recipes Table Name
        public static final String TABLE_NAME = "recipes";

        //"_ID" column will be PK and the folloing  additional to the following
        public static final String COLUMN_RECIPE_ID = "recipe_id"; // originally "id" in json, uniq key
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";

    }
    public static final class Steps implements BaseColumns {
        //Recipes URI
        public static final Uri STEPS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();
        public static final Uri RECIPE_STEPS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES_STEPS).build();

        //Recipes Table Name
        public static final String TABLE_NAME = "steps";

        //"_ID" column will be PK and the folloing  additional to the following
        public static final String COLUMN_RECIPE_ID = "recipe_id";  // foreign key
        public static final String COLUMN_STEP_ID = "step_id";  /// originally "id" in json
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoURL";
        public static final String COLUMN_THUMB_NAIL_URL = "thumbnailURL";

    }
    public static final class Ingredients implements BaseColumns {
        //Recipes URI
        public static final Uri INGREDIENTS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        //Recipes Table Name
        public static final String TABLE_NAME = "ingredients";

        //"_ID" column will be PK and the following  additional to the following
        public static final String COLUMN_RECIPE_ID = "recipe_id";  // foreign key

        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";


    }

}
