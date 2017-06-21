package com.example.uadnd.cou8901.bakersresourceapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uadnd.cou8901.bakersresourceapp.R;
import com.example.uadnd.cou8901.bakersresourceapp.cp.BakersResourceContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by dd2568 on 6/14/2017.
 */

public class StepCursorAdapter  extends RecyclerView.Adapter<StepCursorAdapter.StepViewHolder>{
    private Cursor mCursor;
    private Context mContext;

    public StepCursorAdapter(Context mContext) {
        Timber.d("StepCursorAdapter");
        this.mContext = mContext;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder");
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.step_layout, parent, false);
        return new StepCursorAdapter.StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Timber.d("onBindViewHolder");
        int recipeIdIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_RECIPE_ID);
        final int stepIdIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_STEP_ID);
        int shortDescriptionIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_SHORT_DESCRIPTION);

        int descriptionIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_DESCRIPTION);
        int videoURLIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_VIDEO_URL);
        int thumbnailURLIndex = mCursor.getColumnIndex(BakersResourceContract.Steps.COLUMN_THUMB_NAIL_URL);

        int idIndex = mCursor.getColumnIndex(BakersResourceContract.Steps._ID);

        mCursor.moveToPosition(position);
        int recipeId = mCursor.getInt(recipeIdIndex);
        final String stepId = mCursor.getString(stepIdIndex);
        final String shortDescription = mCursor.getString(shortDescriptionIndex);
        final String description = mCursor.getString(descriptionIndex);
        final String videoURL = mCursor.getString(videoURLIndex);
        final String thumbnailURL = mCursor.getString(thumbnailURLIndex);
        final String id = mCursor.getString(idIndex); // URL

        holder.mStepView.setText(String.valueOf(stepId + ". " + shortDescription));
        Timber.d("before:setOnClickListener");
        holder.mStepView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Timber.d("onClick");
                //Toast.makeText(mContext, "On Click Listener", Toast.LENGTH_LONG).show();

                Intent recipeStepDetailIntent = new Intent(mContext, RecipeStepDetailActivity.class );
                recipeStepDetailIntent.putExtra("_ID", id);
                Bundle bundle = new Bundle();
                bundle.putString("_ID", id);
                bundle.putString("STEP_ID", stepId );
                bundle.putString("DESCRIPTION", description);
                bundle.putString("SHORT_DESCRIPTION", shortDescription);
                bundle.putString("VIDEO_URL", videoURL);
                bundle.putString("THUMB_NAIL_URL", thumbnailURL);
                recipeStepDetailIntent.putExtra("STEP_BUNDLE", bundle);

                startActivity(mContext, recipeStepDetailIntent, bundle);  // on click start detail activity
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

    class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step) TextView mStepView;
        public StepViewHolder(View view) {
            super(view);
            Timber.d("StepViewHolder");
            ButterKnife.bind(this, view);
        }
    }
}
