package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Intent;
import android.os.Bundle;
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

public class RecipeCursorAdapterWidget extends RecyclerView.Adapter<RecipeCursorAdapterWidget.RecipeViewHolder>{

    private Cursor mCursor;
    private Context mContext;

    public RecipeCursorAdapterWidget(Context mContext) {
        Timber.d("RecipeCursorAdapter");
        this.mContext = mContext;
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder");
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recipe_layout_widget, parent, false);
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
        String name = mCursor.getString(nameIndex);
        String servings = mCursor.getString(servingsIndex);
        String image = mCursor.getString(imageIndex); // URL

        holder.mRecipeNameView.setText(String.valueOf(recipeId + ". " + name));
       Timber.d("before:setOnClickListener");
        holder.mRecipeNameView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Timber.d("onClick");
                //Toast.makeText(mContext, "On Click Listener", Toast.LENGTH_LONG).show();
                Intent recipeIngredientIntent = new Intent(mContext, RecipeIngredientActivityWidget.class );
                recipeIngredientIntent.putExtra("RECIPE_ID", recipeId);
                Bundle bundle = new Bundle();
                bundle.putString("RECIPE_ID", String.valueOf(recipeId));
                startActivity(mContext, recipeIngredientIntent, bundle);  // on click start detail activity
            }
        });
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


        @BindView(R.id.tv_recipe_name_widget) TextView mRecipeNameView;
        public RecipeViewHolder(View view) {
            super(view);
            Timber.d("RecipeViewHolder");
            ButterKnife.bind(this, view);
        }

    }
}
