package com.example.uadnd.cou8901.bakersresourceapp.db;

import java.util.ArrayList;

/**
 * Created by dd2568 on 6/6/2017.
 */

public class Recipes {
    ArrayList<Recipe> recipes;

    public Recipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
    public Recipes() {
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
