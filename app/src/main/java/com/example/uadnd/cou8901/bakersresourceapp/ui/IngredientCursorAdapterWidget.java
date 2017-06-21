package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uadnd.cou8901.bakersresourceapp.R;
import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by dd2568 on 6/14/2017.
 */

public class IngredientCursorAdapterWidget extends RecyclerView.Adapter<IngredientCursorAdapterWidget.IngredientViewHolder>{
    private Cursor mCursor;
    private Context mContext;

    public IngredientCursorAdapterWidget(Context mContext) {
        Timber.d("IngredientCursorAdapter");
        this.mContext = mContext;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder");
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.ingredient_layout_widget, parent, false);
        return new IngredientCursorAdapterWidget.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        Timber.d("onBindViewHolder");
        int recipeIdIndex = mCursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_RECIPE_ID);
        int quantityIndex = mCursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_QUANTITY);
        int measureIndex = mCursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_MEASURE);
        int ingredientIndex = mCursor.getColumnIndex(BakersResourceContract.Ingredients.COLUMN_INGREDIENT);

        mCursor.moveToPosition(position);
        int recipeId = mCursor.getInt(recipeIdIndex);
        String quantity = mCursor.getString(quantityIndex);
        String measure = mCursor.getString(measureIndex);
        String ingredient = mCursor.getString(ingredientIndex);

        holder.mIngredientView.setText("\n" + ingredient + " \nQty. " + quantity + " \nMeasure" + measure);

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

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_widget) TextView mIngredientView;

        public IngredientViewHolder(View view) {
            super(view);
            Timber.d("RecipeIngredientHolder");
            ButterKnife.bind(this, view);
        }

    }


}
