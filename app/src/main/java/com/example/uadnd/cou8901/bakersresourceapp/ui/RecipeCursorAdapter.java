package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.database.Cursor;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uadnd.cou8901.bakersresourceapp.R;
import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.support.v4.content.ContextCompat.startActivity;


/**
 * Created by dd2568 on 6/12/2017.
 */

public class RecipeCursorAdapter extends RecyclerView.Adapter<RecipeCursorAdapter.RecipeViewHolder>{

    private Cursor mCursor;
    private Context mContext;

    public RecipeCursorAdapter(Context mContext) {
        Timber.d("RecipeCursorAdapter");
        this.mContext = mContext;
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder");
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recipe_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Timber.d("onBindViewHolder");
        int recipeIdIndex = mCursor.getColumnIndex(BakersResourceContract.Recipes.COLUMN_RECIPE_ID);
        int nameIndex = mCursor.getColumnIndex(BakersResourceContract.Recipes.COLUMN_NAME);
        int servingsIndex = mCursor.getColumnIndex(BakersResourceContract.Recipes.COLUMN_SERVINGS);
        int imageIndex = mCursor.getColumnIndex(BakersResourceContract.Recipes.COLUMN_IMAGE);

        mCursor.moveToPosition(position);
        final int recipeId = mCursor.getInt(recipeIdIndex);
        final String name = mCursor.getString(nameIndex);
        String servings = mCursor.getString(servingsIndex);
        String image = mCursor.getString(imageIndex); // URL

        holder.mRecipeNameView.setText(String.valueOf(recipeId + ". " + name));
        //Images are missing in json but in case the data changes
        //  http://image.tmdb.org/t/p/w185/y4MBh0EjBlMuOzv9axM4qJlmhzz.jpg
        //image = "http://image.tmdb.org/t/p/w185/y4MBh0EjBlMuOzv9axM4qJlmhzz.jpg";
        if(!image.equals("")) {
            Picasso.with(mContext).load(image).fit().into(holder.mRecipeImageView);
        } /*else {
            holder.mRecipeImageView.setImageResource(R.drawable.sample_bake_image);
        }*/
        Timber.d("before:setOnClickListener");
        holder.mRecipeImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Timber.d("onClick");
                //Toast.makeText(mContext, "On Click Listener", Toast.LENGTH_LONG).show();
                //Let us use to save the recipe_id that we visited latest and use it to server ingredients for the widget
                writeToSharedPref(mContext.getString(R.string.key_favorite_recipe_id), String.valueOf(recipeId));
                writeToSharedPref(mContext.getString(R.string.key_favorite_recipe_name), name);
                Intent recipeStepIntent = new Intent(mContext, StepListActivity.class );
                recipeStepIntent.putExtra("RECIPE_ID", recipeId);
                Bundle bundle = new Bundle();
                bundle.putString("RECIPE_ID", String.valueOf(recipeId));
                startActivity(mContext, recipeStepIntent, bundle);  // on click start detail activity
            }
        });
        holder.mRecipeNameView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Timber.d("onClick");
                //Toast.makeText(mContext, "On Click Listener", Toast.LENGTH_LONG).show();
                //Intent recipeStepIntent = new Intent(mContext, RecipeStepActivity.class );  // Design changed to Mastere Detail Flow with Fragment
                //Review 1
                writeToSharedPref(mContext.getString(R.string.key_favorite_recipe_id), String.valueOf(recipeId));
                Intent recipeStepIntent = new Intent(mContext, StepListActivity.class );
                recipeStepIntent.putExtra("RECIPE_ID", recipeId);
                Bundle bundle = new Bundle();
                bundle.putString("RECIPE_ID", String.valueOf(recipeId));
                startActivity(mContext, recipeStepIntent, bundle);  // on click start detail activity
            }
        });
    }

    private void writeToSharedPref(String key, String val) {

        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.app_shared_pref_file), Context.MODE_PRIVATE) ; //getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        Timber.d("getItemCount");
        if (mCursor == null) {
            Timber.d("getItemCount : mCursor == null");
            return 0;
        }
        Timber.d("getItemCount=" + mCursor.getCount());
        return mCursor.getCount();

    }

    public Cursor swapCursor(Cursor cursor) {
        if(mCursor == cursor) {
            return null;  // No change in cursor
        }
        Cursor tempCursor = mCursor;
        this.mCursor = cursor;

        if(cursor != null) {
            this.notifyDataSetChanged();
        }
        return tempCursor;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_image) ImageView mRecipeImageView;
        @BindView(R.id.tv_recipe_name) TextView mRecipeNameView;
        public RecipeViewHolder(View view) {
            super(view);
            Timber.d("RecipeViewHolder");
            ButterKnife.bind(this, view);
        }
    }
}
